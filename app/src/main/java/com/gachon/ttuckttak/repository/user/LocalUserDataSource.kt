package com.gachon.ttuckttak.repository.user

import com.gachon.ttuckttak.data.local.entity.Diagnosis
import com.gachon.ttuckttak.data.local.entity.UserProfile
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes

interface LocalUserDataSource {

    suspend fun savePasswordResetEmail(email: String)

    suspend fun getPasswordResetEmail(): String?

    suspend fun getUserProfile(): UserProfile?

    suspend fun saveUserInfo(data: UserInfoRes)

    suspend fun getRecentDiagnosis(): Diagnosis?

    suspend fun getPushStatus(): Boolean

    suspend fun getNightPushStatus(): Boolean

    suspend fun updateLocalPushStatus(targetValue: Boolean)

    suspend fun updateLocalNightPushStatus(targetValue: Boolean)
}