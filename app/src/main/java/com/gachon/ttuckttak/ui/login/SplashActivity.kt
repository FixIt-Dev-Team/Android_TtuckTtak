package com.gachon.ttuckttak.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.gachon.ttuckttak.ui.login.SplashViewmodel.NavigateTo.*
import com.gachon.ttuckttak.ui.main.StartActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewmodel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
    }

    private fun setObservers() {
        viewModel.viewEvent.observe(this) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Landing -> startActivity(Intent(this, LandingActivity::class.java))
                    is Start -> startActivity(Intent(this, StartActivity::class.java))
                }
            }
        }
    }
}