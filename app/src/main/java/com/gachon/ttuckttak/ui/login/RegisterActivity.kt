package com.gachon.ttuckttak.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.gachon.ttuckttak.BuildConfig
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.LoginRes
import com.gachon.ttuckttak.databinding.ActivityRegisterBinding
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.kakao.sdk.common.KakaoSdk
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// FixMe: Deprecated된 함수들 최신화

class RegisterActivity : BaseActivity<ActivityRegisterBinding>(ActivityRegisterBinding::inflate) {

    private val userManager: UserManager by lazy { UserManager(this) }
    private val tokenManager: TokenManager by lazy { TokenManager(applicationContext) }

    private lateinit var googleApiClient: GoogleApiClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY) // Local.properties에서 값을 가져와 초기화하기

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this,
                object : GoogleApiClient.OnConnectionFailedListener {
                    override fun onConnectionFailed(connectionResult: ConnectionResult) {
                        // 연결 실패 처리
                        Log.e(TAG, "구글 연결 실패")
                        showToast("로그인 실패!")
                    }
                })
            .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
            .build()
    }

    override fun initAfterBinding() = with(binding) {
        buttonKakaoLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, KakaoLoginWebViewActivity::class.java)
            startActivityForResult(intent, REQ_KAKAO_AUTH_CODE)
        }

        buttonGoogleLogin.setOnClickListener {
            val intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
            startActivityForResult(intent, REQ_GOOGLE_OPEN_ID)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_KAKAO_AUTH_CODE && resultCode == Activity.RESULT_OK) {
            val authCode = data?.getStringExtra("authCode")
            if (authCode != null) {
                Log.i(TAG, "Received kakao auth code: $authCode")
                loginWithOauth(param = authCode, method = LoginMethod.KAKAO)
            }
        }

        if (requestCode == REQ_GOOGLE_OPEN_ID) {
            val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data as Intent)

            if (result != null && result.isSuccess) {
                val idToken = result.signInAccount?.idToken

                if (idToken != null) {
                    Log.i(TAG, "구글 id token: $idToken")
                    loginWithOauth(param = idToken, method = LoginMethod.GOOGLE)
                }
                else {
                    Log.e(TAG, "구글 id token 발급 실패")
                    showToast("로그인 실패")
                }

            } else {
                Log.e(TAG, "구글 로그인 실패")
                showToast("로그인 실패")
            }
        }
    }

    /***
     * Oauth를 이용한 로그인
     *
     * param: 카카오 인가코드 or 구글 open id token
     * method: 카카오 or 구글
     */
    private fun loginWithOauth(param: String, method: LoginMethod) {
        CoroutineScope(Dispatchers.IO).launch {
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
                        saveInfo(data = result.data)

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
        const val REQ_KAKAO_AUTH_CODE = 100
        const val REQ_GOOGLE_OPEN_ID = 101
        const val TAG = "LOGIN"
    }

    enum class LoginMethod {
        KAKAO, GOOGLE
    }
}