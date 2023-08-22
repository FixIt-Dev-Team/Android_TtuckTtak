package com.gachon.ttuckttak.data.remote.dto.solution

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SolutionDto(
    @SerializedName("solutionIdx")
    val solIdx: String,
    @SerializedName("issueType")
    val issueType: Int,
    @SerializedName("descHeader")
    val descHeader: String,
    @SerializedName("level")
    val level: Int
): Serializable