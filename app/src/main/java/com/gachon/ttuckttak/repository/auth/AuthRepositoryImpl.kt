package com.gachon.ttuckttak.repository.auth

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.auth.EmailConfirmRes
import com.gachon.ttuckttak.data.remote.dto.auth.LoginRes
import com.gachon.ttuckttak.data.remote.dto.auth.LogoutRes
import com.gachon.ttuckttak.data.remote.dto.auth.RefreshRes
import com.gachon.ttuckttak.data.remote.dto.auth.SignUpReq
import com.gachon.ttuckttak.data.remote.dto.member.PutPwEmailRes
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val localAuthDataSource: LocalAuthDataSource,
    private val remoteAuthDataSource: RemoteAuthDataSource
) : AuthRepository {

    // ---------- Local ----------

    override suspend fun checkAccessTokenExist(): Boolean =
        localAuthDataSource.checkAccessTokenExist()

    override suspend fun updateTokenInfo(token: RefreshRes) =
        localAuthDataSource.updateTokenInfo(token)

    override suspend fun saveUserInfo(data: LoginRes) = localAuthDataSource.saveUserInfo(data)

    override suspend fun clearUserInfo() = localAuthDataSource.clearUserInfo()

    // ---------- Remote ----------

    override suspend fun emailConfirm(email: String): BaseResponse<EmailConfirmRes> =
        remoteAuthDataSource.emailConfirm(email)

    override suspend fun signUp(data: SignUpReq): BaseResponse<LoginRes> =
        remoteAuthDataSource.signUp(data)

    override suspend fun login(email: String, pw: String): BaseResponse<LoginRes> =
        remoteAuthDataSource.login(email, pw)

    override suspend fun loginWithKakaoAccount(authCode: String): BaseResponse<LoginRes> =
        remoteAuthDataSource.loginWithKakaoAccount(authCode)

    override suspend fun loginWithGoogleAccount(idToken: String): BaseResponse<LoginRes> =
        remoteAuthDataSource.loginWithGoogleAccount(idToken)

    override suspend fun updateAccessToken(): BaseResponse<RefreshRes> =
        remoteAuthDataSource.updateAccessToken()

    override suspend fun findAccount(email: String): BaseResponse<PutPwEmailRes> =
        remoteAuthDataSource.findAccount(email)

    override suspend fun logout(): BaseResponse<LogoutRes> = remoteAuthDataSource.logout()
}