package com.gachon.ttuckttak.repository

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.dto.auth.EmailConfirmRes
import com.gachon.ttuckttak.data.remote.dto.auth.LoginReq
import com.gachon.ttuckttak.data.remote.dto.auth.LoginRes
import com.gachon.ttuckttak.data.remote.dto.auth.LogoutReq
import com.gachon.ttuckttak.data.remote.dto.auth.LogoutRes
import com.gachon.ttuckttak.data.remote.service.AuthService
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authService: AuthService,
    private val tokenManager: TokenManager,
    private val userManager: UserManager
) : AuthRepository {

    override suspend fun emailConfirm(email: String): BaseResponse<EmailConfirmRes> {
        return authService.emailConfirm(email)
    }

    override suspend fun login(email: String, pw: String): BaseResponse<LoginRes> {
        return authService.login(LoginReq(userId = email, userPw = pw))
    }

    override suspend fun logout(): BaseResponse<LogoutRes> {
        return authService.logout(LogoutReq(userManager.getUserIdx()!!))
    }

    override suspend fun saveUserInfo(data: LoginRes) {
        userManager.saveUserIdx(data.userIdx)
        tokenManager.saveToken(data.tokenInfo)
    }

    override suspend fun clearUserInfo() {
        userManager.clearUserIdx()
        tokenManager.clearToken()
    }
}