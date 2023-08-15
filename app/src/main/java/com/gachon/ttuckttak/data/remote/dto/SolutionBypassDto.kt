package com.gachon.ttuckttak.data.remote.dto

data class SolutionBypassDto (
    val bypassIdx: String,
    val startEntryIdx: Int,
    val targetEntryIdx: Int,
    val targetEntryName: String
)