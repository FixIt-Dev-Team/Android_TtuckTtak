package com.gachon.ttuckttak.data.local.entity

import androidx.room.ColumnInfo

data class UserProfile(
    @ColumnInfo(name = "user_name") val userName: String?,
    val email: String?,
    @ColumnInfo(name = "profile_img_url") val profileImgUrl: String?
)