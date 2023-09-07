package com.gachon.ttuckttak.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "user_diagnosis")
data class Diagnosis(
    val item: String, // 진단 항목
    val time: Date = Date(), // 진단 시간. 현재 시간을 기본값으로 사용
    val bypassIdx: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0L
}