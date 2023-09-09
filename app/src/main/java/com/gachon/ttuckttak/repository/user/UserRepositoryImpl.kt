package com.gachon.ttuckttak.repository.user

import androidx.lifecycle.LiveData
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.entity.Diagnosis
import com.gachon.ttuckttak.data.local.entity.UserProfile
import com.gachon.ttuckttak.data.remote.dto.member.NicknameRes
import com.gachon.ttuckttak.data.remote.dto.member.NoticeRes
import com.gachon.ttuckttak.data.remote.dto.member.PutPwEmailRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoUpdateRes
import okhttp3.MultipartBody
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val localUserDataSource: LocalUserDataSource,
    private val remoteUserDataSource: RemoteUserDataSource
) : UserRepository {

    // ---------- Local ----------

    override suspend fun savePasswordResetEmail(email: String) =
        localUserDataSource.savePasswordResetEmail(email)

    override suspend fun getPasswordResetEmail(): String? =
        localUserDataSource.getPasswordResetEmail()

    override fun getLocalUserProfile(): LiveData<UserProfile> =
        localUserDataSource.getUserProfile()

    override suspend fun updateUserInfo(data: UserInfoRes) = localUserDataSource.updateUserInfo(data)

    override fun getRecentDiagnosis(): LiveData<Diagnosis> =
        localUserDataSource.getRecentDiagnosis()

    override suspend fun getPushStatus(): Boolean = localUserDataSource.getPushStatus()

    override suspend fun getNightPushStatus(): Boolean = localUserDataSource.getNightPushStatus()

    override suspend fun updateLocalPushStatus(targetValue: Boolean) =
        localUserDataSource.updateLocalPushStatus(targetValue)

    override suspend fun updateLocalNightPushStatus(targetValue: Boolean) =
        localUserDataSource.updateLocalNightPushStatus(targetValue)

    // ---------- Remote ----------

    override suspend fun getUserInfo(): BaseResponse<UserInfoRes> =
        remoteUserDataSource.getUserInfo()

    override suspend fun checkNicknameAvailable(nickname: String): BaseResponse<NicknameRes> =
        remoteUserDataSource.checkNicknameAvailable(nickname)

    override suspend fun updateRemoteUserProfile(
        newNickname: String,
        newProfileImg: MultipartBody.Part?
    ): BaseResponse<UserInfoUpdateRes> =
        remoteUserDataSource.updateRemoteUserProfile(newNickname, newProfileImg)

    override suspend fun updateRemotePushStatus(targetValue: Boolean): BaseResponse<NoticeRes> =
        remoteUserDataSource.updateRemotePushStatus(targetValue)

    override suspend fun updateRemoteNightPushStatus(targetValue: Boolean): BaseResponse<NoticeRes> =
        remoteUserDataSource.updateRemoteNightPushStatus(targetValue)

    override suspend fun changePassword(): BaseResponse<PutPwEmailRes> =
        remoteUserDataSource.changePassword()

}