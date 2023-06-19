package com.gachon.ttuckttak

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.databinding.ActivityLoginBinding
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.LoginRes
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private val TAG = "LOGIN"
    private val userManager: UserManager by lazy { UserManager(this) }
    private val tokenManager: TokenManager by lazy { TokenManager(applicationContext) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        KakaoSdk.init(this, BuildConfig.KAKAO_NATIVE_APP_KEY) // Local.properties에서 값을 가져와 초기화하기

        // 이미 token이 있는 경우
//        if (AuthApiClient.instance.hasToken()) {
//            UserApiClient.instance.accessTokenInfo { _, error ->
//                if (error == null) {
//                    nextMainActivity()
//                }
//            }
//        }
    }

    override fun initAfterBinding() {
        binding.buttonKakaoLogin.setOnClickListener {
            kakaoLogin()
        }
    }

    private fun kakaoLogin() {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                loginWithKakao(token.accessToken)
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)

                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    loginWithKakao(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    /**
     * 생성된 카카오톡 인가 코드를 이용해 뚝딱 서비스만의 token 발급받기
     */
    private fun loginWithKakao(authorizationCode: String) {
        CoroutineScope(Dispatchers.Main).launch {
            Log.i(TAG, "카카오톡 인가 코드를 사용해 뚝딱 서비스 로그인을 시도합니다.")
            val result = TtukttakServer.loginWithKakao(authorizationCode)

            // 카카오톡 인가 코드를 사용해 정상적으로 뚝딱 서비스에 로그인한 경우
            if (result.isSuccess) {
                Log.i(TAG, "카카오톡 인가 코드를 사용하여 뚝딱 서비스에 로그인 하였습니다")
                Log.i(TAG, result.toString())

                // 사용자 식별자와 토큰 정보를 앱에 저장
                saveInfo(data = result.data!!)
                Log.i(TAG, "사용자 식별자와 토큰 정보를 저장하였습니다.")

                // Todo: 다음 화면으로 넘어가기
            }

            // 카카오톡 인가 코드를 사용해 정상적으로 뚝딱 서비스에 로그인하지 못한 경우
            else {
                Log.e(TAG, "카카오톡 인가 코드를 사용하여 뚝딱 서비스에 로그인 하는 데 실패하였습니다")
                Log.e(TAG, "${result.code} ${result.message}")
                Toast.makeText(this@LoginActivity, result.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * 뚝딱 서비스의 사용자 식별자와 토큰 정보를 앱 내에 저장
     */
    private fun saveInfo(data: LoginRes) {
        userManager.saveUserIdx(data.userIdx)
        tokenManager.saveToken(data.tokenInfo)
    }
}