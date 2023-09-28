package com.gachon.ttuckttak.data.remote.dto.auth

data class LoginRes(
    val userIdx: String,
    val tokenInfo: TokensDto
)