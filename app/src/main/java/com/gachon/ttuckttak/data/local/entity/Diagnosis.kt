package com.gachon.ttuckttak.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Diagnosis(
    @PrimaryKey val userIdx: String,
    val context: String,
    val date: String
)