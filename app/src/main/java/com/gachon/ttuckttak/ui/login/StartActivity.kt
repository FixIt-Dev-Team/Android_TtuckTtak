package com.gachon.ttuckttak.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.BuildConfig
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun initAfterBinding() = with(binding) {
        buttonKakaoLogin.setOnClickListener {
            val intent = Intent(this@StartActivity, KakaoLoginWebViewActivity::class.java)
            kakaoLoginLauncher.launch(intent)
        }

        buttonGoogleLogin.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            googleLoginLauncher.launch(intent)
        }
    }

    private val kakaoLoginLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getStringExtra("authCode")?.let { authCode ->
                Log.i(TAG, "Received kakao auth code: $authCode")
                loginWithOauth(param = authCode, method = LoginMethod.KAKAO)
            }
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