package com.gachon.ttuckttak.repository.user

import com.gachon.ttuckttak.data.local.AuthManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.local.dao.DiagnosisDao
import com.gachon.ttuckttak.data.local.dao.UserDao
import com.gachon.ttuckttak.data.local.entity.Diagnosis
import com.gachon.ttuckttak.data.local.entity.User
import com.gachon.ttuckttak.data.local.entity.UserProfile
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes
import javax.inject.Inject

class LocalUserDataSourceImpl @Inject constructor(
    private val userDao: UserDao,
    private val diagnosisDao: DiagnosisDao,
    private val authManager: AuthManager,
    private val userManager: UserManager
) : LocalUserDataSource {

    override suspend fun savePasswordResetEmail(email: String) {
        userManager.savePasswordResetEmail(email)
    }

    override suspend fun getPasswordResetEmail(): String? {
        return userManager.getPasswordResetEmail()
    }

    override suspend fun getUserProfile(): UserProfile? {
        return userDao.getUserProfile()
    }

    override suspend fun saveUserInfo(data: UserInfoRes) {
        userDao.insertUser(
            User(
                userIdx = authManager.getUserIdx()!!,
                userName = data.userName,
                email = data.email,
                profileImgUrl = data.profileImgUrl,
                accountType = data.accountType,
                pushStatus = data.pushStatus,
                nightPushStatus = data.nightPushStatus
            )
        )
    }

    override suspend fun getRecentDiagnosis(): Diagnosis? {
        return diagnosisDao.getDiagnosis()
    }

    override suspend fun getPushStatus(): Boolean {
        return userDao.getEventOrFunctionUpdateNotification()
    }

    override suspend fun getNightPushStatus(): Boolean {
        return userDao.getNightPushNotification()
    }

    override suspend fun updateLocalPushStatus(targetValue: Boolean) {
        userDao.updateEventOrFunctionUpdateNotification(targetValue)
    }

    override suspend fun updateLocalNightPushStatus(targetValue: Boolean) {
        userDao.updateNightPushNotification(targetValue)
    }
}