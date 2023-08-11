package com.gachon.ttuckttak.data.remote.dto

data class RefreshRes(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
)