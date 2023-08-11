package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.EmailConfirmRes
import com.gachon.ttuckttak.data.remote.dto.LoginReq
import com.gachon.ttuckttak.data.remote.dto.LoginRes
import com.gachon.ttuckttak.data.remote.dto.LogoutReq
import com.gachon.ttuckttak.data.remote.dto.LogoutRes
import com.gachon.ttuckttak.data.remote.dto.RefreshReq
import com.gachon.ttuckttak.data.remote.dto.RefreshRes
import com.gachon.ttuckttak.data.remote.dto.SignUpReq
import retrofit2.http.*

interface AuthService {

    @GET("auths/oauth2/kakao")
    suspend fun kakaoLogin(
        @Query("code") authCode: String,
    ): BaseResponse<LoginRes>

    @POST("auths/oauth2/google")
    suspend fun googleLogin(
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