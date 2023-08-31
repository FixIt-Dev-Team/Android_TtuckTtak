package com.gachon.ttuckttak.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FindPwViewmodel @Inject constructor(
    private val userManager: UserManager,
    private val authRepository: AuthRepository
) : BaseViewModel() {

    private val _email = MutableLiveData<String>()
    val email: LiveData<String>
        get() = _email

    private val _showErrorMessage = MutableLiveData<Boolean>()
    val showErrorMessage: LiveData<Boolean>
        get() = _showErrorMessage

    private val _showToastEvent = MutableSharedFlow<String>()
    val showToastEvent = _showToastEvent.asSharedFlow()

    fun findAccount(email: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = authRepository.findAccount(email) // 서버에 계정 찾기 요청

            if (res.isSuccess) { // 계정 찾기에 성공한 경우
                userManager.saveUserMail(email) // sharedPreference에 사용자의 이메일 저장
                viewEvent(NavigateTo.ResetPw) // 다음 activity로 전환 되도록 event를 준다

            } else { // 계정 찾기에 실패한 경우
                _showErrorMessage.postValue(res.code == 400) // 400에러인 경우 화면에 error message를 보여달라고 event를 준다
                _showToastEvent.emit(res.message) // 서버에서 보내 준 에러 메시지로 수정
            }

        } catch (e: Exception) { // 요청에 실패한 경우
            _showToastEvent.emit("요청에 실패하였습니다.") // 요청 실패했다고 메시지 수정
        }
    }

    fun goBack() = viewEvent(NavigateTo.Before)

    sealed class NavigateTo {
        object Before : NavigateTo()
        object ResetPw : NavigateTo()
    }

}