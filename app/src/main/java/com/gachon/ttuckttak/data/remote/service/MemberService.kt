package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.PutPwEmailRes
import retrofit2.http.PUT
import retrofit2.http.Query

interface MemberService {
    @PUT("members/password/email")
    suspend fun changePw(
        @Query("target-email") email: String
    ): BaseResponse<PutPwEmailRes>
}