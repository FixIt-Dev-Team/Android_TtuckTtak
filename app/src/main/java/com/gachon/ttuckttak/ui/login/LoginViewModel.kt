package com.gachon.ttuckttak.ui.login

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.LoginRes
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val TAG = "Login"

    private val ttukttakServer = TtukttakServer
    private val userManager = UserManager(application.applicationContext)
    private val tokenManager = TokenManager(application.applicationContext)

    /**
     * 카카오톡 인가코드 받기
     */
    fun requestKakaoAuthorizationCode(onSuccess: (String) -> Unit, onFailure: (String) -> Unit) {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                onSuccess(token.accessToken)
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context = getApplication<Application>().applicationContext)) {
            UserApiClient.instance.loginWithKakaoTalk(context = getApplication<Application>().applicationContext) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(
                        context = getApplication<Application>().applicationContext,
                        callback = callback
                    )

                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    onSuccess(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(
                context = getApplication<Application>().applicationContext,
                callback = callback
            )
        }
    }

    /**
     * 카카오 로그인
     */
    fun loginWithKakao(
        authorizationCode: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        viewModelScope.launch {
            val result = ttukttakServer.loginWithKakao(authorizationCode)
            handleLoginResponse(result, onSuccess, onFailure)
        }
    }

    /**
     * 뚝딱 서비스에 로그인
     */
    private fun handleLoginResponse(
        result: BaseResponse<LoginRes>,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ) {
        if (result.isSuccess) {
            saveInfo(data = result.data!!)
            onSuccess()
        } else {
            onFailure("${result.code} ${result.message}")
        }
    }

    /**
     * 내부 저장소에 뚝딱 서비스의 사용자 식별자와 토큰 저장
     */
    private fun saveInfo(data: LoginRes) {
        userManager.saveUserIdx(data.userIdx)
        tokenManager.saveToken(data.tokenInfo)
    }
}
