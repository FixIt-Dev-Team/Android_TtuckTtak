package com.gachon.ttuckttak.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gachon.ttuckttak.data.local.entity.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("SELECT * FROM User")
    fun getUser() : User

    @Update
    fun updateAccessToken(user: User)
    @Update
    fun updateRefreshToken(user: User)
    @Update
    fun updateAlertEvent(user: User)
    @Update
    fun updateAlertNight(user: User)

    @Delete
    fun deleteAccessToken(user: User)
    @Delete
    fun deleteUser(user: User)
}