package com.gachon.ttuckttak.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gachon.ttuckttak.data.local.dao.DiagnosisDao
import com.gachon.ttuckttak.data.local.entity.Diagnosis

@Database(entities = [Diagnosis::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun diagnosisDao(): DiagnosisDao
}
