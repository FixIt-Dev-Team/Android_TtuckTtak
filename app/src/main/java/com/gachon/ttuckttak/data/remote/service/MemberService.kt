package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.member.NicknameRes
import com.gachon.ttuckttak.data.remote.dto.member.NoticeReq
import com.gachon.ttuckttak.data.remote.dto.member.NoticeRes
import com.gachon.ttuckttak.data.remote.dto.member.PutPwEmailRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoUpdateRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Query

interface MemberService {
    @PUT("members/password/email")
    suspend fun changePw(
        @Query("target-email") email: String
    ): BaseResponse<PutPwEmailRes>

    @PATCH("members/push")
    suspend fun eventAlert(
        @Header("Authorization") token: String,
        @Body noticeReq: NoticeReq
    ): BaseResponse<NoticeRes>

    @PATCH("members/push/night")
    suspend fun nightAlert(
        @Header("Authorization") token: String,
        @Body noticeReq: NoticeReq
    ): BaseResponse<NoticeRes>

    @GET("members/nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname: String,
    ): BaseResponse<NicknameRes>

    @Multipart
    @PATCH("members/updateprofile")
    suspend fun updateUserInfo(
        @Header("Authorization") token: String,
        @Part("ReqDto") reqDto : RequestBody,
        @Part file: MultipartBody.Part?
    ): BaseResponse<UserInfoUpdateRes>
}