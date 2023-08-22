package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionDetailRes
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionEntryReq
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionEntryRes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface SolutionService {

    @GET("solutions/entryIdx")
    suspend fun getSolbyEntry(
        @Query("entryIdx") entryIdx: Int,
        @Header("Authorization") authCode: String
    ): BaseResponse<SolutionEntryRes>

    @GET("solutions/detail")
    suspend fun getSolDetail(
        @Query("solutionIdx") solutionIdx: String,
        @Header("Authorization") authCode: String
    ): BaseResponse<SolutionDetailRes>

    @POST("solutions/entry")
    suspend fun getSolEntries(
        @Header("Authorization") authCode: String,
        @Body solutionEntryReq: SolutionEntryReq
    ): BaseResponse<SolutionEntryRes>
}