package com.gachon.ttuckttak.data.remote.service

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.annotation.NeedToken
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionDetailRes
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionEntryReq
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionEntryRes
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface SolutionService {

    @NeedToken
    @GET("solutions/entryIdx")
    suspend fun getSolbyEntry(
        @Query("entryIdx") entryIdx: Int,
    ): BaseResponse<SolutionEntryRes>

    @NeedToken
    @GET("solutions/detail")
    suspend fun getSolDetail(
        @Query("solutionIdx") solutionIdx: String,
    ): BaseResponse<SolutionDetailRes>

    @NeedToken
    @POST("solutions/entry")
    suspend fun getSolEntries(
        @Body solutionEntryReq: SolutionEntryReq
    ): BaseResponse<SolutionEntryRes>
}