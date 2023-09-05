package com.gachon.ttuckttak.repository.user

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.entity.UserProfile
import com.gachon.ttuckttak.data.remote.dto.member.NicknameRes
import com.gachon.ttuckttak.data.remote.dto.member.NoticeRes
import com.gachon.ttuckttak.data.remote.dto.member.PutPwEmailRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoUpdateRes
import okhttp3.MultipartBody

interface UserRepository {

    // ---------- Local ----------

    suspend fun savePasswordResetEmail(email: String)

    suspend fun getPasswordResetEmail(): String?

    suspend fun getLocalUserProfile(): UserProfile?

    suspend fun saveUserInfo(data: UserInfoRes)

    suspend fun getPushStatus(): Boolean

    suspend fun getNightPushStatus(): Boolean

    suspend fun updateLocalPushStatus(targetValue: Boolean)

    suspend fun updateLocalNightPushStatus(targetValue: Boolean)

    // ---------- Remote ----------

    suspend fun getRemoteUserProfile(): BaseResponse<UserInfoRes>

    suspend fun checkNicknameAvailable(nickname: String): BaseResponse<NicknameRes>

    suspend fun updateRemoteUserProfile(
        newNickname: String,
        newProfileImg: MultipartBody.Part?
    ): BaseResponse<UserInfoUpdateRes>

    suspend fun updateRemotePushStatus(targetValue: Boolean): BaseResponse<NoticeRes>

    suspend fun updateRemoteNightPushStatus(targetValue: Boolean): BaseResponse<NoticeRes>

    suspend fun changePassword(): BaseResponse<PutPwEmailRes>
}