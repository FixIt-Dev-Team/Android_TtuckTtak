package com.gachon.ttuckttak.repository

import android.util.Log
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.AuthManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.local.dao.UserDao
import com.gachon.ttuckttak.data.local.entity.User
import com.gachon.ttuckttak.data.local.entity.UserProfile
import com.gachon.ttuckttak.data.remote.dto.member.NicknameRes
import com.gachon.ttuckttak.data.remote.dto.member.NoticeReq
import com.gachon.ttuckttak.data.remote.dto.member.NoticeRes
import com.gachon.ttuckttak.data.remote.dto.member.PutPwEmailRes
import com.gachon.ttuckttak.data.remote.dto.view.ProfileDto
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoUpdateRes
import com.gachon.ttuckttak.data.remote.service.MemberService
import com.gachon.ttuckttak.data.remote.service.ViewService
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.ConnectException
import java.net.SocketTimeoutException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val memberService: MemberService,
    private val viewService: ViewService,
    private val authManager: AuthManager,
    private val userManager: UserManager
) : UserRepository {

    override suspend fun savePasswordResetEmail(email: String) {
        userManager.savePasswordResetEmail(email)
    }

    override suspend fun getPasswordResetEmail(): String? {
        return userManager.getPasswordResetEmail()
    }

    /**
     * 사용자 프로필을 가져오는 method
     * 로컬 DB에서 먼저 사용자 정보를 가져온 후, 비동기적으로 서버에서 최신 데이터를 요청한다.
     *
     * @return UserProfile? - 사용자 프로필. 만약 로컬 DB 접근이나 다른 예외로 인해 사용자 정보를 가져오지 못하면 null을 반환한다.
     */
    override suspend fun getUserProfile(): UserProfile? {
        var userProfile: UserProfile? = null

        try {
            userProfile = userDao.getUserProfile() // Local 저장소에서 사용자 정보를 가져온다.
            Log.d("localUserProfile", userProfile.toString())

            // 네트워크 작업을 비동기로 실행
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    viewService.getUserInfo(authManager.getUserIdx()!!)
                        .also { response -> // 서버에 사용자의 프로필 정보를 요청한다.
                            if (response.isSuccess && response.data != null) { // 성공적으로 사용자의 프로필을 가져온 경우
                                saveUserInfo(response.data) // Local 저장소에 사용자의 정보를 저장하고
                                userProfile = response.data.getProfile() // 사용자 프로필을 갱신한다.
                                Log.d("remoteUserProfile", userProfile.toString())
                            }
                        }

                } catch (e: SocketTimeoutException) { // 시간 초과 처리
                    // 시간 초과 했을 경우에는 이미 Local에서 가져온 데이터가 있으므로 별도의 처리 없이 무시
                } catch (e: ConnectException) { // 연결 실패 처리
                    // 네트워크 연결 실패 시에는 이미 Local에서 가져온 데이터가 있으므로 별도의 처리 없이 무시
                } catch (e: Exception) {
                    Log.e("UserRepositoryImpl", e.message.toString()) // 에러 로깅
                    e.printStackTrace() // 에러 로깅
                }
            }

        } catch (e: Exception) {
            Log.e("UserRepositoryImpl", e.message.toString()) // 에러 로깅
            e.printStackTrace() // 에러 로깅
        }

        return userProfile  // 바로 로컬 데이터 반환
    }

    override suspend fun saveUserInfo(data: UserInfoRes) {
        userDao.insertUser(
            User(
                userIdx = authManager.getUserIdx()!!,
                userName = data.userName,
                email = data.email,
                profileImgUrl = data.profileImgUrl,
                accountType = data.accountType,
                pushStatus = data.pushStatus,
                nightPushStatus = data.nightPushStatus
            )
        )
    }

    override suspend fun checkNicknameAvailable(nickname: String): BaseResponse<NicknameRes> {
        return memberService.checkNickname(nickname)
    }

    override suspend fun updateRemoteUserProfile(
        newNickname: String,
        newProfileImg: MultipartBody.Part?
    ): BaseResponse<UserInfoUpdateRes> {

        val profileDto = ProfileDto(
            memberIdx = authManager.getUserIdx()!!,
            nickName = newNickname
        )

        val jsonBody = Gson()
            .toJson(profileDto)
            .toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        return memberService.updateUserInfo(
            reqDto = jsonBody,
            file = newProfileImg
        )
    }

    override suspend fun getPushStatus(): Boolean {
        return userDao.getEventOrFunctionUpdateNotification()
    }

    override suspend fun getNightPushStatus(): Boolean {
        return userDao.getNightPushNotification()
    }

    override suspend fun updateRemotePushStatus(targetValue: Boolean): BaseResponse<NoticeRes> {
        return memberService.eventAlert(
            noticeReq = NoticeReq(memberIdx = authManager.getUserIdx()!!, targetValue)
        )
    }

    override suspend fun updateLocalPushStatus(targetValue: Boolean) {
        userDao.updateEventOrFunctionUpdateNotification(targetValue)
    }

    override suspend fun updateRemoteNightPushStatus(targetValue: Boolean): BaseResponse<NoticeRes> {
        return memberService.nightAlert(
            noticeReq = NoticeReq(memberIdx = authManager.getUserIdx()!!, targetValue)
        )
    }

    override suspend fun updateLocalNightPushStatus(targetValue: Boolean) {
        userDao.updateNightPushNotification(targetValue)
    }

    override suspend fun changePassword(): BaseResponse<PutPwEmailRes> {
        return memberService.changePw(userDao.getUserEmail())
    }
}