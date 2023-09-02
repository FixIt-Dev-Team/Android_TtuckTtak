package com.gachon.ttuckttak.repository

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
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
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

    override suspend fun getUserProfile(): UserProfile? {
        var profile = userDao.getUserProfile() // Local 저장소에 사용자의 프로필이 있는지 조회한다.

        if (profile == null) { // 만약 사용자의 프로필이 존재하지 않는 경우
            profile = try {
                val res = viewService.getUserInfo(
                    userID = authManager.getUserIdx()!!,
                ) // 서버에 요청한다.

                if (res.isSuccess) { // 성공적으로 사용자의 프로필을 가져온 경우
                    saveUserInfo(res.data!!) // Local 저장소에 사용자의 정보를 저장하고
                    res.data.getProfile() // 사용자의 프로필을 설정한다

                } else { // 사용자의 프로필을 가져오는데 실패한 경우 null값으로 설정한다
                    null
                }

            } catch (e: Exception) { // 사용자의 프로필을 설정하는 모든 과정에서 예외가 터지면 null로 설정한다
                null
            }
        }

        return profile // 사용자의 프로필을 반환한다
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