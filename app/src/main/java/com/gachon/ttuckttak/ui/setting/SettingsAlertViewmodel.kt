package com.gachon.ttuckttak.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsAlertViewmodel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    private val _pushStatus = MutableLiveData<Boolean>()
    val pushStatus: LiveData<Boolean>
        get() = _pushStatus

    private val _nightPushStatus = MutableLiveData<Boolean>()
    val nightPushStatus: LiveData<Boolean>
        get() = _nightPushStatus

    private val _showToastEvent = MutableSharedFlow<String>()
    val showToastEvent = _showToastEvent.asSharedFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _pushStatus.postValue(userRepository.getPushStatus())
            _nightPushStatus.postValue(userRepository.getNightPushStatus())
        }
    }

    fun updateEventOrFunctionUpdateNotification() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val targetValue = !_pushStatus.value!! // 바꾸고 싶은 알림 상태
            val res = userRepository.updateRemotePushStatus(targetValue) // 서버에 알림값 설정 변경을 요청한다

            if (res.isSuccess) { // 서버에 정상적으로 반영된 경우
                userRepository.updateLocalPushStatus(targetValue) // 내부 저장소에 업데이트 해주고
                _pushStatus.postValue(targetValue) // 알림 값을 바꿔준다

            } else { // 서버에 정상적으로 반영되지 않은 경우
                _showToastEvent.emit(res.message) // 서버에서 보내 준 에러 메시지로 수정
            }

        } catch (e: Exception) { // 이 과정에서 오류가 발생한 경우
            _showToastEvent.emit("예상치 못한 에러가 발생하였습니다.") // 예상치 못한 에러가 발생했다고 메시지 수정
        }
    }

    fun updateNightPushNotification() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val targetValue = !_nightPushStatus.value!! // 바꾸고 싶은 알림 상태
            val res = userRepository.updateRemoteNightPushStatus(targetValue) // 서버에 알림값 설정 변경을 요청한다

            if (res.isSuccess) { // 서버에 정상적으로 반영된 경우
                userRepository.updateLocalNightPushStatus(targetValue) // 내부 저장소에 업데이트 해주고
                _nightPushStatus.postValue(targetValue) // 알림 값을 바꿔준다

            } else { // 서버에 정상적으로 반영되지 않은 경우
                _showToastEvent.emit(res.message) // 서버에서 보내 준 에러 메시지로 수정
            }

        } catch (e: Exception) { // 이 과정에서 오류가 발생한 경우
            _showToastEvent.emit("예상치 못한 에러가 발생하였습니다.") // 예상치 못한 에러가 발생했다고 메시지 수정
        }
    }

    fun goBack() = viewEvent(NavigateTo.Before)

    sealed class NavigateTo {
        object Before : NavigateTo()
    }

}