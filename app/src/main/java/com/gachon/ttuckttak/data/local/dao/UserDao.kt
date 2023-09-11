package com.gachon.ttuckttak.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gachon.ttuckttak.data.local.entity.User
import com.gachon.ttuckttak.data.local.entity.UserProfile

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Query("SELECT user_name, email, profile_img_url FROM User Where uid = :userIdx")
    fun getUserProfile(userIdx: String): LiveData<UserProfile>

    @Update
    fun updateUserProfile(user: User)

    @Query("Select email from User Where uid = :userIdx")
    suspend fun getUserEmail(userIdx: String): String

    @Query("SELECT push_status FROM User Where uid = :userIdx")
    fun getEventOrFunctionUpdateNotification(userIdx: String): LiveData<Boolean>

    @Query("SELECT night_push_status FROM User Where uid = :userIdx")
    fun getNightPushNotification(userIdx: String): LiveData<Boolean>

    @Query("Update User set push_status = :targetValue Where uid = :userIdx")
    suspend fun updateEventOrFunctionUpdateNotification(userIdx: String, targetValue: Boolean)

    @Query("Update User set night_push_status = :targetValue Where uid = :userIdx")
    suspend fun updateNightPushNotification(userIdx: String, targetValue: Boolean)

    @Query("Delete from User Where uid = :userIdx")
    suspend fun deleteUser(userIdx: String)
}