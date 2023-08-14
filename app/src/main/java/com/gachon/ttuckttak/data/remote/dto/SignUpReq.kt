package com.gachon.ttuckttak.data.remote.dto

data class SignUpReq(
    val userId: String,
    val userPw: String,
    val nickname: String,
    val adProvision: Boolean
)
