package com.gachon.ttuckttak.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gachon.ttuckttak.data.local.entity.User
import com.gachon.ttuckttak.data.local.entity.UserProfile

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Query("SELECT userName, email, profileImgUrl FROM User")
    fun getUserProfile(): UserProfile?

    @Query("Select email from User")
    fun getUserEmail(): String

    @Query("SELECT pushStatus FROM User")
    fun getEventOrFunctionUpdateNotification(): Boolean

    @Query("SELECT nightPushStatus FROM User")
    fun getNightPushNotification(): Boolean

    @Query("Update User set pushStatus=:targetValue")
    fun updateEventOrFunctionUpdateNotification(targetValue: Boolean)

    @Query("Update User set nightPushStatus=:targetValue")
    fun updateNightPushNotification(targetValue: Boolean)

    @Query("Delete from User")
    fun deleteUser()
}