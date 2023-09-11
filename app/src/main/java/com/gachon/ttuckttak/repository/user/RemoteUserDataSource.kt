package com.gachon.ttuckttak.repository.user

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.member.NicknameRes
import com.gachon.ttuckttak.data.remote.dto.member.NoticeRes
import com.gachon.ttuckttak.data.remote.dto.member.PutPwEmailRes
import com.gachon.ttuckttak.data.remote.dto.view.UserAlertStatusInfoRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoUpdateRes
import okhttp3.MultipartBody

interface RemoteUserDataSource {

    suspend fun getUserInfo(): BaseResponse<UserInfoRes>

    suspend fun getUserAlertStatusInfo(): BaseResponse<UserAlertStatusInfoRes>

    suspend fun checkNicknameAvailable(nickname: String): BaseResponse<NicknameRes>
    suspend fun updateRemoteUserProfile(
        newNickname: String,
        newProfileImg: MultipartBody.Part?
    ): BaseResponse<UserInfoUpdateRes>

    suspend fun updateRemotePushStatus(targetValue: Boolean): BaseResponse<NoticeRes>

    suspend fun updateRemoteNightPushStatus(targetValue: Boolean): BaseResponse<NoticeRes>

    suspend fun changePassword(): BaseResponse<PutPwEmailRes>

}