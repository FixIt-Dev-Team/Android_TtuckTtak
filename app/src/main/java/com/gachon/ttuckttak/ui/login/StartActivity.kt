package com.gachon.ttuckttak.ui.login

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.util.Log
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.BuildConfig
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.LoginRes
import com.gachon.ttuckttak.databinding.ActivityStartBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.common.KakaoSdk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class StartActivity : BaseActivity<ActivityStartBinding>(ActivityStartBinding::inflate) {

    private val userManager: UserManager by lazy { UserManager(this) }
    private val tokenManager: TokenManager by lazy { TokenManager(applicationContext) }
    private lateinit var googleSignInClient: GoogleSignInClient
    lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // 1. TextView 참조
        textView = findViewById(R.id.text_welcome1)

        // 2. String 문자열 데이터 취득
        val textData: String = textView.text.toString()

        // 3. SpannableStringBuilder 타입으로 변환
        val builder = SpannableStringBuilder(textData)

        // 4-3 해당하는 문자열에 빨간색 적용
        val colorBlueSpan = ForegroundColorSpan(Color.parseColor("#1DB9DB"))
        builder.setSpan(colorBlueSpan, 0, 2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

        // 5. TextView에 적용
        textView.text = builder
    }

    override fun initAfterBinding() = with(binding) {
        imgButtonKakao.setOnClickListener {
            val intent = Intent(this@StartActivity, KakaoLoginWebViewActivity::class.java)
            kakaoLoginLauncher.launch(intent)
        }

        imgButtonGoogle.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            googleLoginLauncher.launch(intent)
        }

        textWelcome3.setOnClickListener {
            startNextActivity(LoginActivity::class.java)
        }
    }

    private val kakaoLoginLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getStringExtra("authCode")?.let { authCode ->
                Log.i(TAG, "카카오 auth code: $authCode")
                loginWithOauth(param = authCode, method = LoginMethod.KAKAO)
            }
        } else {
            Log.e(TAG, "카카오 auth code 발급 실패")
            showToast("로그인 실패")
        }
    }

    private val googleLoginLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        GoogleSignIn.getSignedInAccountFromIntent(result.data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result?.idToken
                if (idToken != null) {
                    Log.i(TAG, "구글 id token: $idToken")
                    loginWithOauth(param = idToken, method = LoginMethod.GOOGLE)
                } else {
                    Log.e(TAG, "구글 id token 발급 실패")
                    showToast("로그인 실패")
                }
            }
        }
    }

    private fun loginWithOauth(param: String, method: LoginMethod) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val result: BaseResponse<LoginRes> = when (method) {
                    LoginMethod.KAKAO -> TtukttakServer.loginWithKakao(authCode = param)
                    LoginMethod.GOOGLE -> TtukttakServer.loginWithGoogle(idToken = param)
                }

                withContext(Dispatchers.Main) {
                    if (result.isSuccess) {
                        val data = result.data!!
                        Log.i(TAG, "userIdx: ${data.userIdx}")
                        Log.i(TAG, "accessToken: ${data.tokenInfo.accessToken}")
                        Log.i(TAG, "refreshToken: ${data.tokenInfo.refreshToken}")
                        saveInfo(data)

                    } else {
                        Log.e(TAG, "로그인 실패!")
                        Log.e(TAG, "${result.code} ${result.message}")
                        showToast("로그인 실패")
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "서버 통신 오류: ${e.message}")
                    showToast("로그인 실패")
                }
            }
        }
    }

    /**
     * 뚝딱 서비스의 사용자 식별자와 토큰 정보를 앱 내에 저장
     *
     * data: 사용자 정보 - 식별자, access token, refresh token
     */
    private fun saveInfo(data: LoginRes) {
        userManager.saveUserIdx(data.userIdx)
        tokenManager.saveToken(data.tokenInfo)
    }

    companion object {
        const val TAG = "LOGIN"
    }

    enum class LoginMethod {
        KAKAO, GOOGLE
    }
}