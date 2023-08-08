package com.gachon.ttuckttak.data.remote

import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.*
import com.gachon.ttuckttak.data.remote.service.LoginService
import com.gachon.ttuckttak.data.remote.service.MemberService
import com.gachon.ttuckttak.data.remote.service.PushService
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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

    private val loginService: LoginService = retrofit.create(LoginService::class.java)
    private val pushService: PushService = retrofit.create(PushService::class.java)
    private val memberService: MemberService = retrofit.create(MemberService::class.java)

    suspend fun signUp(signupReq: SignUpReq): BaseResponse<LoginRes> = withContext(Dispatchers.IO) {
        loginService.signUp(signupReq)
    }


    suspend fun loginWithKakao(authCode: String): BaseResponse<LoginRes> = withContext(Dispatchers.IO) {
        loginService.kakaoLogin(authCode)
    }

    suspend fun loginWithGoogle(idToken: String): BaseResponse<LoginRes> = withContext(Dispatchers.IO) {
        loginService.googleLogin(idToken)
    }

    suspend fun emailConfirm(email: String) : BaseResponse<EmailConfirmRes> = withContext(Dispatchers.IO) {
        loginService.emailConfirm(email)
    }

    suspend fun push(token: String, noticeReq: NoticeReq): BaseResponse<NoticeRes> = withContext(Dispatchers.IO) {
        pushService.eventAlert(token, noticeReq)
    }

    suspend fun pushNight(token: String, noticeReq: NoticeReq): BaseResponse<NoticeRes> = withContext(Dispatchers.IO) {
        pushService.nightAlert(token, noticeReq)
    }

    suspend fun login(loginReq: LoginReq): BaseResponse<LoginRes> = withContext(Dispatchers.IO) {
        loginService.login(loginReq)
    }

    suspend fun changePw(email: String): BaseResponse<PutPwEmailRes> = withContext(Dispatchers.IO) {
        memberService.changePw(email)
    }
}