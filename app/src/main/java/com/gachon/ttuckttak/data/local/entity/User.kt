package com.gachon.ttuckttak.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey @ColumnInfo(name = "uid") val userIdx: String,
    @ColumnInfo(name = "user_name") val userName: String?,
    val email: String?,
    @ColumnInfo(name = "profile_img_url") val profileImgUrl: String?,
    @ColumnInfo(name = "account_type") val accountType: String?,
    @ColumnInfo(name = "push_status") val pushStatus: Boolean?,
    @ColumnInfo(name = "night_push_status") val nightPushStatus: Boolean?
)