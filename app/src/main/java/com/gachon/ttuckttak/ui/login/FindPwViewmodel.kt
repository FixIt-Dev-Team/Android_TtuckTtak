package com.gachon.ttuckttak.ui.login

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.data.remote.dto.member.PutPwEmailRes
import com.gachon.ttuckttak.repository.auth.AuthRepository
import com.gachon.ttuckttak.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class FindPwViewmodel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    // 서버 응답을 관리하는 StateFlow
    private val _response = MutableStateFlow<BaseResponse<PutPwEmailRes>?>(null)
    val response: StateFlow<BaseResponse<PutPwEmailRes>?> = _response

    private val _showToastEvent = MutableStateFlow<String?>(null)
    val showToastEvent: StateFlow<String?> = _showToastEvent

    fun findAccount(email: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            authRepository.findAccount(email).also { response -> // 서버에 계정 찾기 요청
                _response.value = response // 서버 응답을 StateFlow에 저장한다.

                if (response.isSuccess) { // 계정 찾기에 성공한 경우
                    userRepository.savePasswordResetEmail(email) // sharedPreference에 사용자의 이메일 저장
                    viewEvent(NavigateTo.ResetPw) // 다음 activity로 전환 되도록 event를 준다

                } else { // 계정 찾기에 실패한 경우
                    _showToastEvent.value = response.message // 서버에서 보내 준 에러 메시지로 수정
                }
            }

        } catch (e: Exception) { // 요청에 실패한 경우
            Log.e("FindPwViewmodel", e.message.toString())
            _showToastEvent.value = "요청에 실패하였습니다." // 요청 실패했다고 메시지 수정
        }
    }

    fun goBack() = viewEvent(NavigateTo.Before)

    sealed class NavigateTo {
        object Before : NavigateTo()
        object ResetPw : NavigateTo()
    }

}