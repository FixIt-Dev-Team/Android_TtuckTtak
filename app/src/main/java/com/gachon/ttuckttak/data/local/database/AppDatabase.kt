package com.gachon.ttuckttak.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.gachon.ttuckttak.data.local.dao.UserDao
import com.gachon.ttuckttak.data.local.entity.User

@Database(entities = [User::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var database: AppDatabase? = null
        private const val ROOM_DB = "user.db"

        fun getDatabase(context: Context): AppDatabase {
            return database ?: Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, ROOM_DB
            ).build()
        }
    }
}