package com.gachon.ttuckttak.data.remote.dto.auth

data class SignUpReq(
    val userId: String,
    val userPw: String,
    val nickname: String,
    val adProvision: Boolean
)
