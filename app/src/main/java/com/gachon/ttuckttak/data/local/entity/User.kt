package com.gachon.ttuckttak.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User(
    @PrimaryKey
    val userIdx: String,
    val accessToken: String,
    val refreshToken: String,
    val alertEvent: Boolean,
    val alertNight: Boolean
)