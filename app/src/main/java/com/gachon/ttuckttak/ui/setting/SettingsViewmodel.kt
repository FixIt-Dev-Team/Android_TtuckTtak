package com.gachon.ttuckttak.ui.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.data.local.entity.UserProfile
import com.gachon.ttuckttak.repository.auth.AuthRepository
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
class SettingsViewmodel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : BaseViewModel() {

    val profile: LiveData<UserProfile> = userRepository.getLocalUserProfile()

    private val _showToastEvent = MutableSharedFlow<String>()
    val showToastEvent = _showToastEvent.asSharedFlow()

    init {
        updateUserInfo() // 초기화시 사용자의 정보를 갱신한다.
    }

    /**
     * 사용자 정보를 갱신하는 method
     */
    private fun updateUserInfo() = viewModelScope.launch(Dispatchers.IO) {
        try {
            userRepository.getUserInfo()
                .also { response -> // 서버에 사용자의 프로필 정보를 요청한다.
                    if (response.isSuccess) { // 성공적으로 사용자의 정보를 가져온 경우
                        response.data?.let { userInfo ->
                            userRepository.updateUserInfo(userInfo) // Local 저장소에 사용자의 정보를 갱신한다
                        }
                    }
                }

        } catch (e: SocketTimeoutException) {
            // 시간 초과 했을 경우에는 이미 Local에서 가져온 데이터가 있으므로 별도의 처리 없이 무시
        } catch (e: ConnectException) {
            // 네트워크 연결 실패 시에는 이미 Local에서 가져온 데이터가 있으므로 별도의 처리 없이 무시
        } catch (e: Exception) {
            Log.e("SettingsViewmodel", e.message.toString()) // 에러 로깅
            e.printStackTrace() // 에러 로깅
            _showToastEvent.emit("요청에 실패하였습니다.") // 요청 실패했다고 메시지 수정
        }
    }

    fun logout() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val res = authRepository.logout() // 서버에 사용자 프로필 요청한다
            if (res.isSuccess) {
                authRepository.clearUserInfo() // 사용자 폰에 저장되어 있는 사용자의 정보를 지우고
                viewEvent(NavigateTo.Landing) // LandingActivity로 전환되라고 event를 준다

            } else {
                _showToastEvent.emit(res.message) // 서버에서 보내 준 에러 메시지로 수정
            }

        } catch (e: Exception) {
            Log.e("SettingsViewmodel", e.message.toString()) // 에러 로깅
            e.printStackTrace() // 에러 로깅
            _showToastEvent.emit("요청에 실패하였습니다.") // 요청 실패했다고 메시지 수정
        }
    }

    fun goBack() = viewEvent(NavigateTo.Before)

    fun goSettingsAlertActivity() = viewEvent(NavigateTo.SettingsAlert)

    fun goSettingsProfileActivity() = viewEvent(NavigateTo.SettingsProfile)

    sealed class NavigateTo {
        object Before : NavigateTo()
        object SettingsAlert : NavigateTo()
        object SettingsProfile : NavigateTo()
        object Landing : NavigateTo()
    }

}