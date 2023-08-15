package com.gachon.ttuckttak.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gachon.ttuckttak.data.local.dao.DiagnosisDao
import com.gachon.ttuckttak.data.local.entity.Diagnosis

@Database(entities = [Diagnosis::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun diagnosisDao(): DiagnosisDao

    companion object {
        private var database: AppDatabase? = null

        // Singleton pattern
        fun getInstance(context: Context): AppDatabase? {
            if (database == null) {
                // synchronized : 중복 방지
                synchronized(AppDatabase::class) {
                    database = Room.databaseBuilder(context.applicationContext,
                        AppDatabase::class.java, "room.db")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return database!!
        }
    }
}
