package com.gachon.ttuckttak.ui.login

import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandingViewmodel @Inject constructor(
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _showToastEvent = MutableSharedFlow<String>()
    val showToastEvent = _showToastEvent.asSharedFlow()

    fun loginWithKakaoAccount(authCode: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = authRepository.loginWithKakaoAccount(authCode) // 서버에 카카오 계정으로 로그인 요청을 한다

            if (res.isSuccess) { // 카카오 계정으로 로그인에 성공한 경우
                authRepository.saveUserInfo(data = res.data!!) // 사용자의 정보(식별자, 토큰)를 저장한다
                viewEvent(NavigateTo.Start) // 다음 activity로 전환 되도록 event를 준다

            } else { // 카카오 계정으로 로그인에 실패한 경우
                _showToastEvent.emit(res.message) // 서버에서 보내 준 에러 메시지로 수정
            }

        } catch (e: Exception) { // 요청에 실패한 경우
            _showToastEvent.emit("요청에 실패하였습니다.") // 요청 실패했다고 메시지 수정
        }
    }

    fun loginWithGoogleAccount(idToken: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = authRepository.loginWithGoogleAccount(idToken) // 서버에 구글 계정으로 로그인 요청을 한다

            if (res.isSuccess) { // 구글 계정으로 로그인에 성공한 경우
                authRepository.saveUserInfo(data = res.data!!) // 사용자의 정보(식별자, 토큰)를 저장한다
                viewEvent(NavigateTo.Start) // 다음 activity로 전환 되도록 event를 준다

            } else { // 구글 계정으로 로그인에 실패한 경우
                _showToastEvent.emit(res.message) // 서버에서 보내 준 에러 메시지로 수정
            }

        } catch (e: Exception) { // 요청에 실패한 경우
            _showToastEvent.emit("요청에 실패하였습니다.") // 요청 실패했다고 메시지 수정
        }
    }

    fun goJoinPart1Activity() = viewEvent(NavigateTo.JoinPart1)

    fun goLoginActivity() = viewEvent(NavigateTo.Login)

    sealed class NavigateTo {
        object Start : NavigateTo()
        object JoinPart1 : NavigateTo()

        object Login : NavigateTo()
    }

}