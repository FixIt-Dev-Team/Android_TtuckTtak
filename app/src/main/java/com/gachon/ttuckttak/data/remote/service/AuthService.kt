package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.auth.EmailConfirmRes
import com.gachon.ttuckttak.data.remote.dto.auth.LoginReq
import com.gachon.ttuckttak.data.remote.dto.auth.LoginRes
import com.gachon.ttuckttak.data.remote.dto.auth.LogoutReq
import com.gachon.ttuckttak.data.remote.dto.auth.LogoutRes
import com.gachon.ttuckttak.data.remote.dto.auth.RefreshReq
import com.gachon.ttuckttak.data.remote.dto.auth.RefreshRes
import com.gachon.ttuckttak.data.remote.dto.auth.SignUpReq
import retrofit2.http.*

interface AuthService {

    @GET("auths/oauth2/kakao")
    suspend fun loginWithKakao(
        @Query("code") authCode: String,
    ): BaseResponse<LoginRes>

    @POST("auths/oauth2/google")
    suspend fun loginWithGoogle(
        @Header("Google-id-token") idToken: String,
    ): BaseResponse<LoginRes>

    @POST("auths/email-confirm")
    suspend fun emailConfirm(
        @Query("to") email: String
    ): BaseResponse<EmailConfirmRes>

    @POST("auths/login")
    suspend fun login(
        @Body loginReq: LoginReq
    ): BaseResponse<LoginRes>

    @POST("auths/signup")
    suspend fun signUp(
        @Body signupReq: SignUpReq
    ): BaseResponse<LoginRes>

    @POST("auths/logout")
    suspend fun logout(
        @Body logoutReq: LogoutReq
    ): BaseResponse<LogoutRes>

    @POST("auths/token/refresh")
    suspend fun refreshAccessToken(
        @Body refreshReq: RefreshReq
    ): BaseResponse<RefreshRes>
}