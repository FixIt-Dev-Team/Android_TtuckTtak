package com.gachon.ttuckttak.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.repository.auth.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewmodel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {

    private val _loginFail = MutableLiveData<Boolean>()
    val loginFail: LiveData<Boolean>
        get() = _loginFail

    private val _showToastEvent = MutableSharedFlow<String>()
    val showToastEvent = _showToastEvent.asSharedFlow()

    fun login(email: String, pw: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            authRepository.login(email, pw).also { response -> // 서버에 로그인 요청
                if (response.isSuccess) { // 로그인에 성공한 경우
                    authRepository.saveUserInfo(data = response.data!!) // 사용자의 정보(식별자, 토큰)를 저장한다
                    viewEvent(NavigateTo.Start) // 다음 activity로 전환 되도록 event를 준다

                } else { // 로그인에 성공하지 못한 경우
                    _loginFail.postValue(true) // 로그인 실패 UI로 수정되도록 이벤트를 준다
                }
            }

        } catch (e: Exception) {
            _showToastEvent.emit("요청에 실패하였습니다.") // 요청 실패했다고 메시지 수정
        }
    }

    fun goBack() = viewEvent(NavigateTo.Before)

    fun findPw() = viewEvent(NavigateTo.FindPw)

    sealed class NavigateTo {
        object Before : NavigateTo()
        object Start : NavigateTo()
        object FindPw : NavigateTo()
    }
}