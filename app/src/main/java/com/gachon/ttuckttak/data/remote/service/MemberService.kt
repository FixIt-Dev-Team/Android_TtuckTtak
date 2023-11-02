package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.annotation.NeedToken
import com.gachon.ttuckttak.data.remote.dto.member.NicknameRes
import com.gachon.ttuckttak.data.remote.dto.member.NoticeReq
import com.gachon.ttuckttak.data.remote.dto.member.NoticeRes
import com.gachon.ttuckttak.data.remote.dto.member.PutPwEmailRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoUpdateRes
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Body
import retrofit2.http.GET
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

    @NeedToken
    @PATCH("members/push")
    suspend fun eventAlert(
        @Body noticeReq: NoticeReq
    ): BaseResponse<NoticeRes>

    @NeedToken
    @PATCH("members/push/night")
    suspend fun nightAlert(
        @Body noticeReq: NoticeReq
    ): BaseResponse<NoticeRes>

    @GET("members/nickname")
    suspend fun checkNickname(
        @Query("nickname") nickname: String,
    ): BaseResponse<NicknameRes>

    @NeedToken
    @Multipart
    @PATCH("members/updateprofile")
    suspend fun updateUserInfo(
        @Part("ReqDto") reqDto : RequestBody,
        @Part file: MultipartBody.Part?
    ): BaseResponse<UserInfoUpdateRes>
}