package com.gachon.ttuckttak.data.remote.dto.auth

data class RefreshReq(
    val refreshToken: String,
    val userIdx: String
)
