package com.gachon.ttuckttak.repository.auth

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.auth.EmailConfirmRes
import com.gachon.ttuckttak.data.remote.dto.auth.LoginRes
import com.gachon.ttuckttak.data.remote.dto.auth.LogoutRes
import com.gachon.ttuckttak.data.remote.dto.auth.RefreshRes
import com.gachon.ttuckttak.data.remote.dto.auth.SignUpReq
import com.gachon.ttuckttak.data.remote.dto.member.PutPwEmailRes

interface AuthRepository {

    // ---------- Local ----------

    suspend fun checkAccessTokenExist(): Boolean

    suspend fun updateTokenInfo(token: RefreshRes)

    suspend fun saveUserInfo(data: LoginRes)

    suspend fun clearUserInfo()

    // ---------- Remote ----------

    suspend fun emailConfirm(email: String): BaseResponse<EmailConfirmRes>

    suspend fun signUp(data: SignUpReq) : BaseResponse<LoginRes>

    suspend fun login(email: String, pw: String): BaseResponse<LoginRes>

    suspend fun loginWithKakaoAccount(authCode: String): BaseResponse<LoginRes>

    suspend fun loginWithGoogleAccount(idToken: String): BaseResponse<LoginRes>

    suspend fun updateAccessToken(): BaseResponse<RefreshRes>

    suspend fun findAccount(email: String): BaseResponse<PutPwEmailRes>

    suspend fun logout(): BaseResponse<LogoutRes>
}