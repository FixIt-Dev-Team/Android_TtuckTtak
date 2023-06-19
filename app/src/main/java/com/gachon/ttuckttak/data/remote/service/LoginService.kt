package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.LoginRes
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {

    @POST("/oauth2/login/kakao")
    suspend fun kakaoLogin(
        @Header("Kakao-auth-code") accessToken: String,
    ): BaseResponse<LoginRes>

    @POST("/oauth2/login/google")
    suspend fun googleLogin(
        @Header("Google-auth-code") accessToken: String,
    ): BaseResponse<LoginRes>
}