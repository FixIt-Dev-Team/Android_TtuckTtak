package com.gachon.ttuckttak.data.remote.dto.solution

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class SolutionDto(
    @SerializedName("sol_idx")
    var solIdx: String,
    @SerializedName("issue_type")
    var issueType: Int,
    @SerializedName("desc_header")
    var descHeader: String,
    @SerializedName("level")
    var level: Int
): Serializable