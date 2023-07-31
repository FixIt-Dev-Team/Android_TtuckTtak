package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.EmailConfirmRes
import com.gachon.ttuckttak.data.remote.dto.LoginRes
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {

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

}