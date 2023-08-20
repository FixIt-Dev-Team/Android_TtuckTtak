package com.gachon.ttuckttak.data.remote

import com.gachon.ttuckttak.data.remote.dto.auth.LoginReq
import com.gachon.ttuckttak.data.remote.dto.auth.LogoutReq
import com.gachon.ttuckttak.data.remote.dto.auth.RefreshReq
import com.gachon.ttuckttak.data.remote.dto.auth.SignUpReq
import com.gachon.ttuckttak.data.remote.dto.member.NoticeReq
import com.gachon.ttuckttak.data.remote.dto.view.ProfileDto
import com.gachon.ttuckttak.data.remote.service.AuthService
import com.gachon.ttuckttak.data.remote.service.MemberService
import com.gachon.ttuckttak.data.remote.service.ViewService
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object TtukttakServer {

    private const val URL = "https://ttukttak.store/api/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(URL)
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
        .client(OkHttpClient.Builder().build())
        .build()

    private val authService: AuthService = retrofit.create(AuthService::class.java)
    private val memberService: MemberService = retrofit.create(MemberService::class.java)
    private val viewService: ViewService = retrofit.create(ViewService::class.java)

    suspend fun signUp(signupReq: SignUpReq) = authService.signUp(signupReq)

    suspend fun loginWithKakao(authCode: String) = authService.kakaoLogin(authCode)

    suspend fun loginWithGoogle(idToken: String) = authService.googleLogin(idToken)

    suspend fun emailConfirm(email: String) = authService.emailConfirm(email)

    suspend fun login(loginReq: LoginReq) = authService.login(loginReq)


    suspend fun logout(logoutReq: LogoutReq) = authService.logout(logoutReq)

    suspend fun refreshAccessToken(refreshReq: RefreshReq) = authService.refreshAccessToken(refreshReq)

    suspend fun push(token: String, noticeReq: NoticeReq) = memberService.eventAlert(token, noticeReq)

    suspend fun pushNight(token: String, noticeReq: NoticeReq) = memberService.nightAlert(token, noticeReq)

    suspend fun changePw(email: String) = memberService.changePw(email)

    suspend fun checkNickname(nickname: String) = memberService.checkNickname(nickname)

    suspend fun getUserInfo(userId: String, authCode: String) = viewService.getUserInfo(userId, authCode)

    suspend fun updateUserInfo(authCode: String, reqDto: ProfileDto, file: MultipartBody.Part?) = viewService.updateUserInfo(authCode, reqDto, file)
}