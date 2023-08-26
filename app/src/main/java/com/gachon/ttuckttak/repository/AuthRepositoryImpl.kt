package com.gachon.ttuckttak.repository

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.auth.EmailConfirmRes
import com.gachon.ttuckttak.data.remote.service.AuthService
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authService: AuthService) : AuthRepository {

    override suspend fun emailConfirm(email: String): BaseResponse<EmailConfirmRes> {
        return authService.emailConfirm(email)
    }
}