package com.gachon.ttuckttak.repository

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.auth.EmailConfirmRes

interface AuthRepository {
    suspend fun emailConfirm(email: String): BaseResponse<EmailConfirmRes>
}