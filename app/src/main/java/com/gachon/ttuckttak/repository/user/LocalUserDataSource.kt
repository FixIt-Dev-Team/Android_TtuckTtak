package com.gachon.ttuckttak.repository.user

import androidx.lifecycle.LiveData
import com.gachon.ttuckttak.data.local.entity.Diagnosis
import com.gachon.ttuckttak.data.local.entity.UserProfile
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes

interface LocalUserDataSource {

    suspend fun savePasswordResetEmail(email: String)

    suspend fun getPasswordResetEmail(): String?

    fun getUserProfile(): LiveData<UserProfile>

    suspend fun updateUserInfo(data: UserInfoRes)

    fun getRecentDiagnosis(): LiveData<Diagnosis>

    suspend fun getPushStatus(): Boolean

    suspend fun getNightPushStatus(): Boolean

    suspend fun updateLocalPushStatus(targetValue: Boolean)

    suspend fun updateLocalNightPushStatus(targetValue: Boolean)
}