package com.gachon.ttuckttak.ui.login

import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withTimeoutOrNull
import javax.inject.Inject

@HiltViewModel
class SplashViewmodel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {

    private val splashViewTime = 1000L
    private val maxUpdateTokenTime = 3000L

    init { // 초기화시
        viewModelScope.launch {
            // splashViewTime 동안 대기하는 작업과 토큰 정보를 갱신하는 작업을 병렬로 시작
            val delayJob = async { delay(splashViewTime) }  // 스플래시 화면이 표시
            val updateTokenJob = async {
                withTimeoutOrNull(maxUpdateTokenTime) { updateTokenInfo() }
            }  // 최대 maxUpdateTokenTime(3초) 동안 토큰 정보 갱신 작업

            delayJob.await()  // (작업 1) 비동기로 1초동안 화면 보여주기

            when (updateTokenJob.await()) { // (작업 2) 비동기로 토큰 업데이트 작업하기
                TokenUpdateResult.Success -> viewEvent(NavigateTo.Start)  // 토큰 갱신을 성공했다면 StartActivity로 전환 되도록 event를 준다
                TokenUpdateResult.Failure, null -> viewEvent(NavigateTo.Landing)  // 토큰 갱신에 실패했다면 LandingActivity로 전환 되도록 event를 준다
            }
        }
    }

    private suspend fun updateTokenInfo(): TokenUpdateResult {
        try {
            if (authRepository.checkAccessTokenExist()) { // 사용자의 access token이 존재하는 경우
                authRepository.updateAccessToken().also { response -> // 서버에 access token 갱신 요청을 한다
                    return if (response.isSuccess && response.data != null) { // 만약 서버로부터 갱신된 토큰의 정보를 받았다면
                        authRepository.updateTokenInfo(response.data) // 사용자의 토큰을 갱신하고
                        TokenUpdateResult.Success // 성공적으로 업데이트 되었다는 결과 반환

                    } else { // 서버로부터 갱신된 토큰의 정보를 받지 못했다면
                        TokenUpdateResult.Failure // 업데이트에 실패했다는 결과 반환
                    }
                }

            } else { // 사용자의 access token이 존재하지 않는 경우
                return TokenUpdateResult.Failure // 업데이트에 실패했다는 결과 반환
            }

        } catch (e: Exception) { // 요청에 실패한 경우
            return TokenUpdateResult.Failure // 업데이트에 실패했다는 결과 반환
        }
    }

    enum class TokenUpdateResult {
        Success, Failure
    }

    sealed class NavigateTo {
        object Start : NavigateTo()
        object Landing : NavigateTo()
    }

}