package com.gachon.ttuckttak

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.gachon.ttuckttak.data.local.UserManager
import androidx.appcompat.app.AppCompatActivity

import com.gachon.ttuckttak.ui.login.LandingActivity
import com.gachon.ttuckttak.ui.login.LoginActivity
import com.gachon.ttuckttak.ui.main.StartActivity


class SplashActivity : AppCompatActivity() {

    val SPLASH_VIEW_TIME: Long = 1000
    private val userManager: UserManager by lazy { UserManager(this@SplashActivity) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({ //delay를 위한 handler

            // user 정보가 있을 경우
            if (userManager.getUserIdx() != null) {
                startActivity(Intent(this@SplashActivity, StartActivity::class.java))
            }
            // user 정보가 없을 경우
            else {
                startActivity(Intent(this@SplashActivity, LandingActivity::class.java))
            }
            finish()
        }, SPLASH_VIEW_TIME)
    }

}