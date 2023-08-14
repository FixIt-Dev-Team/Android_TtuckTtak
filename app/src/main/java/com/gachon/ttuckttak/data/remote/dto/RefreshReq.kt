package com.gachon.ttuckttak.data.remote.dto

data class RefreshReq(
    val refreshToken: String,
    val userIdx: String
)
