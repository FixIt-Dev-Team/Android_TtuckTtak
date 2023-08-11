package com.gachon.ttuckttak

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.gachon.ttuckttak.data.local.UserManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.RefreshReq

import com.gachon.ttuckttak.ui.login.LandingActivity
import com.gachon.ttuckttak.ui.main.StartActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SplashActivity : AppCompatActivity() {

    val SPLASH_VIEW_TIME: Long = 1000
    private val userManager: UserManager by lazy { UserManager(this@SplashActivity) }
    private val tokenManager: TokenManager by lazy { TokenManager(this@SplashActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({ //delay를 위한 handler

            // user 정보가 있을 경우
            if (userManager.getUserIdx() != null) {
                lifecycleScope.launch(Dispatchers.IO) {
                    try {
                        // 서버에 AccessToken 갱신 요청
                        val response = TtukttakServer.refreshAccessToken(RefreshReq(tokenManager.getRefreshToken()!!, userManager.getUserIdx()!!))
                        Log.i("response", response.toString())

                        // 토큰을 갱신하는 데 성공한 경우, 토큰 update 및 startActivity로 이동
                        if (response.isSuccess) {
                            tokenManager.resetAccessToken(response.data!!.accessToken)
                            tokenManager.resetRefreshToken(response.data.refreshToken)
                            startActivity(Intent(this@SplashActivity, StartActivity::class.java))
                        }

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            Log.e("SplashActivity", "서버 통신 오류: ${e.message}")
                            Toast.makeText(this@SplashActivity, "서버 통신 실패", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }

            // token 갱신을 하지 못 한 경우
            startActivity(Intent(this@SplashActivity, LandingActivity::class.java))
            finish()
        }, SPLASH_VIEW_TIME)
    }
}