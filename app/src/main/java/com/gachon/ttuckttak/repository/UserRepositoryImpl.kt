package com.gachon.ttuckttak.repository

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.local.dao.UserDao
import com.gachon.ttuckttak.data.local.entity.User
import com.gachon.ttuckttak.data.local.entity.UserProfile
import com.gachon.ttuckttak.data.remote.dto.member.NoticeReq
import com.gachon.ttuckttak.data.remote.dto.member.NoticeRes
import com.gachon.ttuckttak.data.remote.dto.view.UserInfoRes
import com.gachon.ttuckttak.data.remote.service.MemberService
import com.gachon.ttuckttak.data.remote.service.ViewService
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val memberService: MemberService,
    private val viewService: ViewService,
    private val tokenManager: TokenManager,
    private val userManager: UserManager
) : UserRepository {

    override suspend fun getUserProfile(): UserProfile? {
        var profile = userDao.getUserProfile() // Local 저장소에 사용자의 프로필이 있는지 조회한다.

        if (profile == null) { // 만약 사용자의 프로필이 존재하지 않는 경우
            profile = try {
                val res = viewService.getUserInfo(
                    userID = userManager.getUserIdx()!!,
                    authCode = tokenManager.getAccessToken()!!
                ) // 서버에 요청한다.

                if (res.isSuccess) { // 성공적으로 사용자의 프로필을 가져온 경우
                    saveUserInfo(res.data!!) // Local 저장소에 사용자의 정보를 저장하고
                    res.data.getProfile() // 사용자의 프로필을 설정한다

                } else { // 사용자의 프로필을 가져오는데 실패한 경우 null값으로 설정한다
                    null
                }

            } catch (e: Exception) { // 사용자의 프로필을 설정하는 모든 과정에서 예외가 터지면 null로 설정한다
                null
            }
        }

        return profile // 사용자의 프로필을 반환한다
    }

    private fun saveUserInfo(data: UserInfoRes) {
        userDao.insertUser(
            User(
                userIdx = userManager.getUserIdx()!!,
                userName = data.userName,
                email = data.email,
                profileImgUrl = data.profileImgUrl,
                accountType = data.accountType,
                pushStatus = data.pushStatus,
                nightPushStatus = data.nightPushStatus
            )
        )
    }

    override suspend fun getPushStatus(): Boolean {
        return userDao.getEventOrFunctionUpdateNotification()
    }

    override suspend fun getNightPushStatus(): Boolean {
        return userDao.getNightPushNotification()
    }

    override suspend fun updateRemotePushStatus(targetValue: Boolean): BaseResponse<NoticeRes> {
        return memberService.eventAlert(
            token = tokenManager.getAccessToken()!!,
            noticeReq = NoticeReq(memberIdx = userManager.getUserIdx()!!, targetValue)
        )
    }

    override suspend fun updateLocalPushStatus(targetValue: Boolean) {
        userDao.updateEventOrFunctionUpdateNotification(targetValue)
    }

    override suspend fun updateRemoteNightPushStatus(targetValue: Boolean): BaseResponse<NoticeRes> {
        return memberService.nightAlert(
            token = tokenManager.getAccessToken()!!,
            noticeReq = NoticeReq(memberIdx = userManager.getUserIdx()!!, targetValue)
        )
    }

    override suspend fun updateLocalNightPushStatus(targetValue: Boolean) {
        userDao.updateNightPushNotification(targetValue)
    }
}