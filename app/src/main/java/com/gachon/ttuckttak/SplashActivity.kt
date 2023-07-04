package com.gachon.ttuckttak

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.gachon.ttuckttak.ui.login.StartActivity

class SplashActivity : AppCompatActivity() {

    val SPLASH_VIEW_TIME: Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Handler().postDelayed({ //delay를 위한 handler
            startActivity(Intent(this, StartActivity::class.java))
            finish()
        }, SPLASH_VIEW_TIME)
    }
}
