package com.gachon.ttuckttak.data.remote.dto.view

import com.gachon.ttuckttak.data.local.entity.UserProfile

data class UserInfoRes(
    val userName: String,
    val email: String,
    val profileImgUrl: String,
    val accountType: String,
    val pushStatus: Boolean,
    val nightPushStatus: Boolean
) {
    fun getProfile(): UserProfile {
        return UserProfile(userName, email, profileImgUrl)
    }
}
