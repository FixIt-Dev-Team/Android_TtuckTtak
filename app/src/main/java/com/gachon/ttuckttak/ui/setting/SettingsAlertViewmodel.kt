package com.gachon.ttuckttak.ui.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class SettingsAlertViewmodel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val pushStatus: LiveData<Boolean> = userRepository.getPushStatus()

    val nightPushStatus: LiveData<Boolean> = userRepository.getNightPushStatus()

    private val _showToastEvent = MutableSharedFlow<String>()
    val showToastEvent = _showToastEvent.asSharedFlow()

    init {
        updateUserAlertStatus() // 초기화시 사용자의 알림 상태를 갱신한다.
    }

    /**
     * 사용자의 알림 상태를 갱신하는 method
     */
    private fun updateUserAlertStatus() = viewModelScope.launch(Dispatchers.IO) {
        try {
            userRepository.getUserAlertStatusInfo()
                .also { response -> // 서버에 사용자의 정보를 요청한다.
                    if (response.isSuccess) { // 성공적으로 사용자의 정보를 가져온 경우
                        response.data!!.let { userAlert ->
                            // Local 저장소에 사용자의 알림 상태를 갱신한다
                            userRepository.updateLocalPushStatus(userAlert.pushStatus)
                            userRepository.updateLocalNightPushStatus(userAlert.nightPushStatus)
                        }
                    }
                }

        } catch (e: SocketTimeoutException) {
            // 시간 초과 했을 경우에는 이미 Local에서 가져온 데이터가 있으므로 별도의 처리 없이 무시
        } catch (e: ConnectException) {
            // 네트워크 연결 실패 시에는 이미 Local에서 가져온 데이터가 있으므로 별도의 처리 없이 무시
        } catch (e: Exception) {
            Log.e("SettingsProfileViewmodel", e.message.toString()) // 에러 로깅
            e.printStackTrace() // 에러 로깅
            _showToastEvent.emit("요청에 실패하였습니다.") // 요청 실패했다고 메시지 수정
        }
    }


    fun updateEventOrFunctionUpdateNotification(targetValue: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        try {
            userRepository.updateRemotePushStatus(targetValue) // 서버에 알림값 설정 변경을 요청한다
                .also { response ->
                    if (response.isSuccess) { // 서버에 정상적으로 반영된 경우
                        userRepository.updateLocalPushStatus(targetValue) // 내부 저장소에 업데이트 해준다

                    } else { // 서버에 정상적으로 반영되지 않은 경우
                        _showToastEvent.emit(response.message) // 서버에서 보내 준 에러 메시지로 수정
                    }
                }

        } catch (e: Exception) { // 이 과정에서 오류가 발생한 경우
            _showToastEvent.emit("예상치 못한 에러가 발생하였습니다.") // 예상치 못한 에러가 발생했다고 메시지 수정
        }
    }

    fun updateNightPushNotification(targetValue: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        try {
            userRepository.updateRemoteNightPushStatus(targetValue) // 서버에 알림값 설정 변경을 요청한다
                .also { response ->
                    if (response.isSuccess) { // 서버에 정상적으로 반영된 경우
                        userRepository.updateLocalNightPushStatus(targetValue) // 내부 저장소에 업데이트 해준다

                    } else { // 서버에 정상적으로 반영되지 않은 경우
                        _showToastEvent.emit(response.message) // 서버에서 보내 준 에러 메시지로 수정
                    }
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