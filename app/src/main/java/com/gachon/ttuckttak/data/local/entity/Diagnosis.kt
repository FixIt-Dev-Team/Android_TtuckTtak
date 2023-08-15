package com.gachon.ttuckttak.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Diagnosis(
    @PrimaryKey
    val context: String,
    val date: String
)