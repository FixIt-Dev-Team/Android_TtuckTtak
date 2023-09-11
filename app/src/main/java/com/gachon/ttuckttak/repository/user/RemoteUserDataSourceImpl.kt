package com.gachon.ttuckttak.repository.user

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.AuthManager
import com.gachon.ttuckttak.data.local.dao.UserDao
import com.gachon.ttuckttak.data.remote.dto.member.NicknameRes
import com.gachon.ttuckttak.data.remote.dto.member.NoticeReq
import com.gachon.ttuckttak.data.remote.dto.member.NoticeRes
import com.gachon.ttuckttak.data.remote.dto.member.PutPwEmailRes
import com.gachon.ttuckttak.data.remote.dto.view.ProfileDto
import com.gachon.ttuckttak.data.remote.dto.view.UserAlertStatusInfoRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoUpdateRes
import com.gachon.ttuckttak.data.remote.service.MemberService
import com.gachon.ttuckttak.data.remote.service.ViewService
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class RemoteUserDataSourceImpl @Inject constructor(
    private val authManager: AuthManager,
    private val userDao: UserDao,
    private val memberService: MemberService,
    private val viewService: ViewService,
) : RemoteUserDataSource {

    override suspend fun getUserInfo(): BaseResponse<UserInfoRes> {
        return viewService.getUserInfo(authManager.getUserIdx()!!)
    }

    override suspend fun getUserAlertStatusInfo(): BaseResponse<UserAlertStatusInfoRes> {
        return viewService.getUserAlertStatus(authManager.getUserIdx()!!)
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

    override suspend fun updateRemotePushStatus(targetValue: Boolean): BaseResponse<NoticeRes> {
        return memberService.eventAlert(
            noticeReq = NoticeReq(memberIdx = authManager.getUserIdx()!!, targetValue)
        )
    }

    override suspend fun updateRemoteNightPushStatus(targetValue: Boolean): BaseResponse<NoticeRes> {
        return memberService.nightAlert(
            noticeReq = NoticeReq(memberIdx = authManager.getUserIdx()!!, targetValue)
        )
    }

    override suspend fun changePassword(): BaseResponse<PutPwEmailRes> {
        return memberService.changePw(userDao.getUserEmail(authManager.getUserIdx()!!))
    }

}