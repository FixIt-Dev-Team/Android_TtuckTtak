package com.gachon.ttuckttak.ui.join

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.data.remote.dto.auth.EmailConfirmRes
import com.gachon.ttuckttak.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JoinPart1Viewmodel @Inject constructor(private val authRepository: AuthRepository) : BaseViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    private val _response = MutableLiveData<BaseResponse<EmailConfirmRes>>()
    val response: LiveData<BaseResponse<EmailConfirmRes>>
        get() = _response

    private val _showToastEvent = MutableSharedFlow<String>()
    val showToastEvent = _showToastEvent.asSharedFlow()

    fun sendEmailConfirmation(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _state.postValue(State.Loading) // 현재 상태를 요청 중으로 수정

                val res = authRepository.emailConfirm(email) // 서버에 이메일 인증 요청
                _response.postValue(res) // 요청 결과 저장

                if (res.isSuccess) {
                    _state.postValue(State.Success) // 현재 상태를 정상으로 수정
                    viewEvent(NavigateTo.JoinPart2) // 다음 activity로 전환 되도록 event를 준다

                } else {
                    _state.postValue(State.Error) // 현재 상태를 error로 수정
                    _showToastEvent.emit(res.message) // 서버에서 보내 준 에러 메시지로 수정
                }

            } catch (e: Exception) {
                _state.postValue(State.Error) // 현재 상태를 error로 수정
                _showToastEvent.emit("요청에 실패하였습니다.") // 요청 실패했다고 메시지 수정
            }
        }
    }

    fun goBack() = viewEvent(NavigateTo.Before)

    enum class State {
        Loading, Error, Success
    }

    sealed class NavigateTo {
        object Before : NavigateTo()
        object JoinPart2 : NavigateTo()
    }
}