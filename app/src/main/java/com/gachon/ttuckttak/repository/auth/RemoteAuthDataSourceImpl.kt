package com.gachon.ttuckttak.repository.auth

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.AuthManager
import com.gachon.ttuckttak.data.remote.dto.auth.EmailConfirmRes
import com.gachon.ttuckttak.data.remote.dto.auth.LoginReq
import com.gachon.ttuckttak.data.remote.dto.auth.LoginRes
import com.gachon.ttuckttak.data.remote.dto.auth.LogoutReq
import com.gachon.ttuckttak.data.remote.dto.auth.LogoutRes
import com.gachon.ttuckttak.data.remote.dto.auth.RefreshReq
import com.gachon.ttuckttak.data.remote.dto.auth.RefreshRes
import com.gachon.ttuckttak.data.remote.dto.auth.SignUpReq
import com.gachon.ttuckttak.data.remote.dto.member.PutPwEmailRes
import com.gachon.ttuckttak.data.remote.service.AuthService
import com.gachon.ttuckttak.data.remote.service.MemberService
import javax.inject.Inject

class RemoteAuthDataSourceImpl @Inject constructor(
    private val authService: AuthService,
    private val memberService: MemberService,
    private val authManager: AuthManager,
) : RemoteAuthDataSource {

    override suspend fun emailConfirm(email: String): BaseResponse<EmailConfirmRes> {
        return authService.emailConfirm(email)
    }

    override suspend fun signUp(data: SignUpReq): BaseResponse<LoginRes> {
        return authService.signUp(data)
    }

    override suspend fun login(email: String, pw: String): BaseResponse<LoginRes> {
        return authService.login(LoginReq(userId = email, userPw = pw))
    }

    override suspend fun loginWithKakaoAccount(authCode: String): BaseResponse<LoginRes> {
        return authService.loginWithKakao(authCode)
    }

    override suspend fun loginWithGoogleAccount(idToken: String): BaseResponse<LoginRes> {
        return authService.loginWithGoogle(idToken)
    }

    override suspend fun updateAccessToken(): BaseResponse<RefreshRes> {
        return authService.refreshAccessToken(
            RefreshReq(
                refreshToken = authManager.getRefreshToken()!!,
                userIdx = authManager.getUserIdx()!!
            )
        )
    }

    override suspend fun findAccount(email: String): BaseResponse<PutPwEmailRes> {
        return memberService.changePw(email)
    }

    override suspend fun logout(): BaseResponse<LogoutRes> {
        return authService.logout(
            LogoutReq(
                userIdx = authManager.getUserIdx()!!
            )
        )
    }
}