package com.gachon.ttuckttak.data.remote.dto.solution

import com.google.gson.annotations.SerializedName

data class SolutionEntryRes(
    val entryIdx: Int,
    val level: Int,
    val problemName: String,
    @SerializedName("solutionDtos")
    val solList: List<SolutionDto> = arrayListOf(),
    @SerializedName("solutionPossibleDtos")
    val solPList: List<SolutionPossibleDto> = arrayListOf(),
    @SerializedName("solutionBypassDtos")
    val solBList: List<SolutionBypassDto>? = arrayListOf()
)
