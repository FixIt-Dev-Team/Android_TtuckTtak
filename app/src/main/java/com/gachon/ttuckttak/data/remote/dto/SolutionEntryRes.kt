package com.gachon.ttuckttak.data.remote.dto

import com.google.gson.annotations.SerializedName

data class SolutionEntryRes(
    val entryIdx: Int,
    val level: Int,
    val problemName: String,
    @SerializedName("Solution List")
    var solList: SolutionList,
    @SerializedName("SolutionPossible List")
    val solPList: List<SolutionPossibleDto> = arrayListOf(),
    @SerializedName("SolutionBypass List")
    val solBList: List<SolutionBypassDto>? = arrayListOf()
)
