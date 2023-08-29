package com.gachon.ttuckttak.repository

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.entity.UserProfile
import com.gachon.ttuckttak.data.remote.dto.member.NoticeRes

interface UserRepository {

    suspend fun getUserProfile(): UserProfile?

    suspend fun getPushStatus(): Boolean

    suspend fun getNightPushStatus(): Boolean

    suspend fun updateRemotePushStatus(targetValue: Boolean): BaseResponse<NoticeRes>

    suspend fun updateLocalPushStatus(targetValue: Boolean)

    suspend fun updateRemoteNightPushStatus(targetValue: Boolean): BaseResponse<NoticeRes>

    suspend fun updateLocalNightPushStatus(targetValue: Boolean)
}