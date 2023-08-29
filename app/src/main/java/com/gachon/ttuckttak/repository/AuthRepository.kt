package com.gachon.ttuckttak.repository

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.auth.EmailConfirmRes
import com.gachon.ttuckttak.data.remote.dto.auth.LoginRes
import com.gachon.ttuckttak.data.remote.dto.auth.LogoutRes

interface AuthRepository {
    suspend fun emailConfirm(email: String): BaseResponse<EmailConfirmRes>

    suspend fun login(email: String, pw: String): BaseResponse<LoginRes>

    suspend fun logout(): BaseResponse<LogoutRes>

    suspend fun saveUserInfo(data: LoginRes)

    suspend fun clearUserInfo()
}