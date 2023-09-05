package com.gachon.ttuckttak.ui.setting

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.base.StringResourcesProvider
import com.gachon.ttuckttak.data.local.entity.UserProfile
import com.gachon.ttuckttak.repository.user.UserRepository
import com.gachon.ttuckttak.utils.RegexUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

@HiltViewModel
class SettingsProfileViewmodel @Inject constructor(
    private val userRepository: UserRepository,
    private val stringResourcesProvider: StringResourcesProvider
) : BaseViewModel() {

    // 사용자의 프로필
    private val _profile = MutableLiveData<UserProfile?>()
    val profile: LiveData<UserProfile?>
        get() = _profile

    // 닉네임 에러 메시지. 닉네임이 어떤 이유로 사용할 수 없는지 사용자에게 알려주는 역할을 한다.
    private val _nicknameErrorMessage = MutableLiveData<String?>()
    val nicknameErrorMessage: LiveData<String?>
        get() = _nicknameErrorMessage

    // 사용자가 바꾸고 싶은 닉네임
    private var newNickname: String? = null

    // 사용자가 바꾸고 싶은 프로필 사진
    private var newProfileImg: MultipartBody.Part? = null

    // 사용자가 바꾼 값이 있는지. 이 값으로 저장 버튼을 활성화 시킨다.
    private val _changeDetected = MutableLiveData<Boolean>()
    val changeDetected: LiveData<Boolean>
        get() = _changeDetected

    // toast message에 보여줄 text를 관리하는 event
    private val _showToastEvent = MutableSharedFlow<String>()
    val showToastEvent = _showToastEvent.asSharedFlow()

    init {
        getUserProfile() // 초기화시 사용자의 프로필을 요청한다
    }

    /**
     * 사용자 프로필을 가져오는 method
     * 로컬 DB에서 먼저 사용자 정보를 가져온 후, 비동기적으로 서버에서 최신 데이터를 요청한다.
     */
    private fun getUserProfile() = viewModelScope.launch(Dispatchers.IO) {
        try {
            val localUserProfile = userRepository.getLocalUserProfile() // Local 저장소에서 사용자 정보를 가져온다.
            Log.d("localUserProfile", localUserProfile.toString()) // 로깅

            _profile.postValue(localUserProfile) // 사용자의 프로필을 업데이트 한다.

            // 네트워크 작업을 비동기로 실행
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    userRepository.getRemoteUserProfile()
                        .also { response -> // 서버에 사용자의 프로필 정보를 요청한다.
                            if (response.isSuccess) { // 성공적으로 사용자의 프로필을 가져온 경우
                                response.data?.let { userInfo ->
                                    userRepository.saveUserInfo(userInfo) // Local 저장소에 사용자의 정보를 저장하고

                                    val remoteUserProfile = userInfo.getProfile()
                                    Log.d("remoteUserProfile", remoteUserProfile.toString()) // 로깅
                                    _profile.postValue(remoteUserProfile) // 사용자의 프로필을 업데이트 한다.
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
                }
            }

        } catch (e: Exception) {
            Log.e("SettingsProfileViewmodel", e.message.toString()) // 에러 로깅
            e.printStackTrace() // 에러 로깅
            _showToastEvent.emit("요청에 실패하였습니다.") // 요청 실패했다고 메시지 수정
        }
    }

    /**
     * 변경된 사용자의 프로필 이미지를 저장하는 method
     *
     * @param img 변경하고 싶은 사용자의 프로필 이미지
     */
    fun setNewProfileImg(img: MultipartBody.Part) {
        newProfileImg = img
        _changeDetected.postValue(true) // 변경된 정보가 있다고 값을 수정한다
    }

    /**
     * 사용자의 프로필을 업데이트하는 method
     */
    fun updateUserProfile() = viewModelScope.launch(Dispatchers.IO) {
        try {
            userRepository.updateRemoteUserProfile(
                newNickname = newNickname ?: profile.value!!.userName, // 변경할 닉네임이 없다면 기존 닉네임으로 프로필 변경 요청
                newProfileImg = newProfileImg // 변경할 프로필 이미지. 없을 수 있음
            ).also { response -> // 서버에 프로필 변경 요청

                if (response.isSuccess && response.data != null) { // 성공적으로 프로필이 변경되었다면
                    userRepository.saveUserInfo(response.data.userData) // 최신화된 사용자 정보를 저장하고 (RoomDB)
                    viewEvent(NavigateTo.Before) // 이전 화면으로 전환 되도록 event를 준다

                } else { // 사용자의 프로필이 성공적으로 업데이트되지 못 한 경우
                    _showToastEvent.emit(response.message) // 서버에서 보내 준 에러 메시지로 수정
                }
            }

        } catch (e: Exception) { // 요청에 실패한 경우
            Log.e("SettingsProfileViewmodel", e.message.toString()) // 에러 로깅
            e.printStackTrace() // 에러 로깅
            _showToastEvent.emit("요청에 실패하였습니다.") // 요청 실패했다고 메시지 수정
        }
    }

    /**
     * 사용 가능한 닉네임인지 확인하는 method
     *
     * @param nickname 변경하고 싶은 닉네임
     */
    fun checkNicknameAvailable(nickname: String) = viewModelScope.launch(Dispatchers.IO) {
        try {
            if (!RegexUtil.isValidNicknameFormat(nickname)) { // 닉네임 형식에 맞지 않는 경우
                _nicknameErrorMessage.postValue(stringResourcesProvider.getString(R.string.invalid_nickname)) // 올바르지 않은 닉네임이라고 수정한다
            }

            userRepository.checkNicknameAvailable(nickname) // 서버에 해당 닉네임이 사용 가능한지 조회한다
                .also { response ->
                    if (response.isSuccess) { // 해당 닉네임을 사용할 수 있는지 조회에 성공한 경우
                        if (response.data!!.isAvailable) { // 해당 닉네임을 사용할 수 있는 경우
                            newNickname = nickname // 변경할 닉네임을 저장하고
                            _showToastEvent.emit(stringResourcesProvider.getString(R.string.available_nickname)) // 사용 가능한 닉네임이라고 toast message event를 준다
                            _changeDetected.postValue(true) // 변경된 정보가 있다고 값을 수정한다

                        } else { // 해당 닉네임을 사용할 수 없는 경우
                            _nicknameErrorMessage.postValue(stringResourcesProvider.getString(R.string.overlap_nickname)) // 이미 사용중인 닉네임이라고 수정한다
                            _showToastEvent.emit(stringResourcesProvider.getString(R.string.overlap_nickname)) // 이미 사용중인 닉네임이라고 toast message event를 준다
                        }

                    } else { // 해당 닉네임을 사용할 수 있는지 조회하지 못한 경우
                        _nicknameErrorMessage.postValue(response.message) // 서버에서 보내 준 에러 메시지로 수정
                        _showToastEvent.emit(response.message) // 서버에서 보내 준 에러 메시지로 toast message event를 준다
                    }
                }

        } catch (e: Exception) {
            Log.e("SettingsProfileViewmodel", e.message.toString()) // 에러 로깅
            e.printStackTrace() // 에러 로깅
            _showToastEvent.emit("요청에 실패하였습니다.") // 요청 실패했다고 메시지 수정
        }
    }

    /**
     * 사용자의 비밀번호 변경 요청하는 method
     */
    fun changePassword() = viewModelScope.launch(Dispatchers.IO) {
        try {
            userRepository.changePassword().also { response -> // 서버에 비밀번호 변경 찾기 요청
                if (response.isSuccess && response.data!!.sendSuccess) { // 사용자의 이메일에 성공적으로 보내진 경우
                    userRepository.savePasswordResetEmail(profile.value!!.email) // ResetPwActivity에서 보여질 사용자의 이메일을 저장해주고
                    viewEvent(NavigateTo.ResetPw) // ResetPwActivity로 전환 되도록 event를 준다

                } else { // 사용자의 이메일에 성공적으로 보내지 못 한 경우
                    _showToastEvent.emit(response.message) // 서버에서 보내 준 에러 메시지로 수정
                }
            }

        } catch (e: Exception) { // 요청에 실패한 경우
            Log.e("SettingsProfileViewmodel", e.message.toString()) // 에러 로깅
            e.printStackTrace() // 에러 로깅
            _showToastEvent.emit("요청에 실패하였습니다.") // 요청 실패했다고 메시지 수정
        }
    }

    fun goBack() = viewEvent(NavigateTo.Before)

    sealed class NavigateTo {
        object Before : NavigateTo()
        object ResetPw : NavigateTo()
    }

}