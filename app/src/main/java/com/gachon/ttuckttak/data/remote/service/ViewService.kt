package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.ProfileDto
import com.gachon.ttuckttak.data.remote.dto.UserInfoRes
import com.gachon.ttuckttak.data.remote.dto.UserInfoUpdateRes
import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.Part
import retrofit2.http.Query

interface ViewService {

    @GET("views/setting")
    suspend fun getUserInfo(
        @Query("memberIdx") userID: String,
        @Header("Authorization") authCode: String
    ): BaseResponse<UserInfoRes>

    @Multipart
    @PATCH("views/setting/update")
    suspend fun updateUserInfo(
        @Header("Authorization") authCode: String,
        @Part("ReqDto") reqDto: ProfileDto,
        @Part file: MultipartBody.Part?
    ): BaseResponse<UserInfoUpdateRes>
}