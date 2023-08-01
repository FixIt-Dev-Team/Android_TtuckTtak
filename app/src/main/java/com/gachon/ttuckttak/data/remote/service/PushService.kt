package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.NoticeReq
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH

interface PushService {

    @PATCH("/api/members/push")
    suspend fun eventAlert(
        @Header("Authorization") token:String,
        @Body noticeReq: NoticeReq
    ) : BaseResponse<NoticeReq>

}