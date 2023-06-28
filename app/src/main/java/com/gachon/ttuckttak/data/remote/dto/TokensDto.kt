package com.gachon.ttuckttak.data.remote.dto

data class TokensDto(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
)