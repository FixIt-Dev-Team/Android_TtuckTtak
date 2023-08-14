package com.gachon.ttuckttak.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserInfoRes(
    val userName: String,
    val email: String,
    val profileImgUrl: String,
    val accountType: String,
    @SerializedName("push_approve") val pushApprove: Boolean,
    @SerializedName("night_approve") val nightApprove: Boolean
)
