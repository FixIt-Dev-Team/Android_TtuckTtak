package com.gachon.ttuckttak.repository.auth

import com.gachon.ttuckttak.data.local.AuthManager
import com.gachon.ttuckttak.data.local.dao.UserDao
import com.gachon.ttuckttak.data.remote.dto.auth.LoginRes
import com.gachon.ttuckttak.data.remote.dto.auth.RefreshRes
import javax.inject.Inject

class LocalAuthDataSourceImpl @Inject constructor(
    private val authManager: AuthManager,
    private val userDao: UserDao
) : LocalAuthDataSource {

    override suspend fun checkAccessTokenExist(): Boolean {
        return authManager.getAccessToken() != null
    }

    override suspend fun updateTokenInfo(token: RefreshRes) {
        authManager.updateTokenInfo(token)
    }

    override suspend fun saveUserInfo(data: LoginRes) {
        authManager.saveUserInfo(data)
    }

    override suspend fun clearUserInfo() {
        authManager.clearUserInfo()
        userDao.deleteUser()
    }
}