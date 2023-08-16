package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ViewService {

    @GET("views/setting")
    suspend fun getUserInfo(
        @Query("memberIdx") userID: String,
        @Header("Authorization") authCode: String
    ): BaseResponse<UserInfoRes>
}