package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.annotation.NeedToken
import com.gachon.ttuckttak.data.remote.dto.view.UserAlertStatusInfoRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes
import retrofit2.http.GET
import retrofit2.http.Query

interface ViewService {

    @NeedToken
    @GET("views/setting")
    suspend fun getUserInfo(
        @Query("memberIdx") userID: String,
    ): BaseResponse<UserInfoRes>

    @NeedToken
    @GET("views/setting/push")
    suspend fun getUserAlertStatus(
        @Query("memberIdx") userIdx: String,
    ): BaseResponse<UserAlertStatusInfoRes>

}