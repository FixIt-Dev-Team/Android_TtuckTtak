package com.gachon.ttuckttak.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey val userIdx: String,
    val userName: String,
    val email: String,
    val profileImgUrl: String,
    val accountType: String,
    val pushStatus: Boolean,
    val nightPushStatus: Boolean
)