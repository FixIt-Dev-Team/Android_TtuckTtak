package com.gachon.ttuckttak.repository.user

import androidx.lifecycle.LiveData
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.entity.Diagnosis
import com.gachon.ttuckttak.data.local.entity.UserProfile
import com.gachon.ttuckttak.data.remote.dto.member.NicknameRes
import com.gachon.ttuckttak.data.remote.dto.member.NoticeRes
import com.gachon.ttuckttak.data.remote.dto.member.PutPwEmailRes
import com.gachon.ttuckttak.data.remote.dto.view.UserAlertStatusInfoRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoUpdateRes
import okhttp3.MultipartBody

interface UserRepository {

    // ---------- Local ----------

    suspend fun saveRegistrationEmail(email: String)

    suspend fun getRegistrationEmail(): String?

    suspend fun savePasswordResetEmail(email: String)

    suspend fun getPasswordResetEmail(): String?

    fun getLocalUserProfile(): LiveData<UserProfile>

    suspend fun updateUserInfo(data: UserInfoRes)

    fun getPushStatus(): LiveData<Boolean>

    fun getNightPushStatus(): LiveData<Boolean>

    suspend fun updateLocalPushStatus(targetValue: Boolean)

    suspend fun updateLocalNightPushStatus(targetValue: Boolean)

    // Diagnosis

    fun getRecentDiagnosis(): LiveData<Diagnosis>

    fun getLatestDiagnosis(): LiveData<List<Diagnosis>>

    // ---------- Remote ----------

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