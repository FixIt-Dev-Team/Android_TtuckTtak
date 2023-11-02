package com.gachon.ttuckttak.repository.user

import androidx.lifecycle.LiveData
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

    override suspend fun saveRegistrationEmail(email: String) {
        userManager.saveRegistrationEmail(email)
    }

    override suspend fun getRegistrationEmail(): String? {
        return userManager.getRegistrationEmail()
    }

    override suspend fun savePasswordResetEmail(email: String) {
        userManager.savePasswordResetEmail(email)
    }

    override suspend fun getPasswordResetEmail(): String? {
        return userManager.getPasswordResetEmail()
    }

    override fun getUserProfile(): LiveData<UserProfile> {
        return userDao.getUserProfile(authManager.getUserIdx()!!)
    }

    override suspend fun updateUserInfo(data: UserInfoRes) {
        userDao.updateUserProfile(
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

    override fun getPushStatus(): LiveData<Boolean> {
        return userDao.getEventOrFunctionUpdateNotification(authManager.getUserIdx()!!)
    }

    override fun getNightPushStatus(): LiveData<Boolean> {
        return userDao.getNightPushNotification(authManager.getUserIdx()!!)
    }

    override suspend fun updateLocalPushStatus(targetValue: Boolean) {
        userDao.updateEventOrFunctionUpdateNotification(authManager.getUserIdx()!!, targetValue)
    }

    override suspend fun updateLocalNightPushStatus(targetValue: Boolean) {
        userDao.updateNightPushNotification(authManager.getUserIdx()!!, targetValue)
    }

    // Diagnosis
    override fun getRecentDiagnosis(): LiveData<Diagnosis> {
        return diagnosisDao.getLatestDiagnosis(authManager.getUserIdx()!!)
    }

    override fun getLatestDiagnosis(): LiveData<List<Diagnosis>> {
        return diagnosisDao.getAllDiagnoses(authManager.getUserIdx()!!)
    }
}