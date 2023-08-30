package com.gachon.ttuckttak.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.BuildConfig
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityLandingBinding
import com.gachon.ttuckttak.ui.join.JoinPart1Activity
import com.gachon.ttuckttak.ui.main.StartActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

import com.gachon.ttuckttak.ui.login.LandingViewmodel.NavigateTo.*

@AndroidEntryPoint
class LandingActivity : BaseActivity<ActivityLandingBinding>(
    ActivityLandingBinding::inflate,
    TransitionMode.HORIZONTAL
) {

    private val viewModel: LandingViewmodel by viewModels()

    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_CLIENT_ID)
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        setObservers()
        setClickListener()
    }

    private fun setObservers() {
        viewModel.viewEvent.observe(this@LandingActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Start -> startNextActivity(StartActivity::class.java)
                    is Login -> startNextActivity(LoginActivity::class.java)
                    is JoinPart1 -> startNextActivity(JoinPart1Activity::class.java)
                }
            }
        }

        // 일회성 show toast
        lifecycleScope.launch {
            viewModel.showToastEvent.collect { message ->
                showToast(message)
            }
        }
    }

    private fun setClickListener() = with(binding) {
        imagebuttonKakao.setOnClickListener {
            val intent = Intent(this@LandingActivity, KakaoLoginWebViewActivity::class.java)
            kakaoLoginLauncher.launch(intent)
        }

        imagebuttonGoogle.setOnClickListener {
            val intent = googleSignInClient.signInIntent
            googleLoginLauncher.launch(intent)
        }
    }

    private val kakaoLoginLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.getStringExtra("authCode")?.let { authCode ->
                Log.i("LOGIN", "카카오 auth code: $authCode")
                viewModel.loginWithKakaoAccount(authCode)
            }

        } else {
            Log.e("LOGIN", "카카오 auth code 발급 실패")
            showToast("로그인 실패")
        }
    }

    private val googleLoginLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        GoogleSignIn.getSignedInAccountFromIntent(result.data).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val idToken = task.result?.idToken
                if (idToken != null) {
                    Log.i("LOGIN", "구글 id token: $idToken")
                    viewModel.loginWithGoogleAccount(idToken)

                } else {
                    Log.e("LOGIN", "구글 id token 발급 실패")
                    showToast("로그인 실패")
                }
            }
        }
    }
}