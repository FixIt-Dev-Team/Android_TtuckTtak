package com.gachon.ttuckttak.data.remote.dto

data class LoginRes(
    val userIdx: String,
    val tokenInfo: TokensDto
)