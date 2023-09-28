package com.gachon.ttuckttak.data.remote.dto.auth

data class TokensDto(
    val grantType: String,
    val accessToken: String,
    val refreshToken: String
)