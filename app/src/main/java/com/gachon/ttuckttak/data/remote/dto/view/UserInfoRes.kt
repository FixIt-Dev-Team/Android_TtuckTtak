package com.gachon.ttuckttak.data.remote.dto.view

data class UserInfoRes(
    val userName: String,
    val email: String,
    val profileImgUrl: String,
    val accountType: String,
    val pushStatus: Boolean,
    val nightPushStatus: Boolean
)
