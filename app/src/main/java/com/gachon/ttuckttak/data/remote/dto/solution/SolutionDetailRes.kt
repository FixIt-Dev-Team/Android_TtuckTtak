package com.gachon.ttuckttak.data.remote.dto.solution

data class SolutionDetailRes(
    val detailIdx: String,
    val detailHeader: String,
    val desc_header: List<String> = arrayListOf(),
    val content: String,
    val subContent: String
)
