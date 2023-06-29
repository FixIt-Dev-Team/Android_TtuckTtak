package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.LoginRes
import retrofit2.http.Header
import retrofit2.http.POST

interface LoginService {

    @POST("auths/oauth2/login/kakao")
    suspend fun kakaoLogin(
        @Header("Kakao-auth-code") authCode: String,
    ): BaseResponse<LoginRes>
}