package com.gachon.ttuckttak.repository.auth

import com.gachon.ttuckttak.data.remote.dto.auth.LoginRes
import com.gachon.ttuckttak.data.remote.dto.auth.RefreshRes

interface LocalAuthDataSource {

    suspend fun checkAccessTokenExist(): Boolean

    suspend fun updateTokenInfo(token: RefreshRes)

    suspend fun saveUserInfo(data: LoginRes)

    suspend fun clearUserInfo()
}