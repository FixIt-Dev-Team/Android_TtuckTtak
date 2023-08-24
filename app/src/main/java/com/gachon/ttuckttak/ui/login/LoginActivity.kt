package com.gachon.ttuckttak.ui.login

import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.dto.auth.LoginReq
import com.gachon.ttuckttak.data.remote.dto.auth.LoginRes
import com.gachon.ttuckttak.data.remote.service.AuthService
import com.gachon.ttuckttak.databinding.ActivityLoginBinding
import com.gachon.ttuckttak.ui.main.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate, TransitionMode.VERTICAL) {

    @Inject lateinit var userManager: UserManager
    @Inject lateinit var tokenManager: TokenManager
    @Inject lateinit var authService: AuthService

    override fun initAfterBinding() {
        setClickListener()
        setFocusChangeListener()
    }

    private fun setClickListener() = with(binding) {
        buttonBack.setOnClickListener { finish() }
        buttonLogin.setOnClickListener { requestLogin() }
        textFindIdOrPw.setOnClickListener { startNextActivity(FindPwActivity::class.java) }
    }

    private fun requestLogin() = with(binding) {
        val email = edittextEmail.text.toString()
        val pw = editTextPassword.text.toString()

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // 서버에 이메일 인증코드 전송 요청
                val response = authService.login(LoginReq(email, pw))
                Log.i("response", response.toString())

                if (response.isSuccess) {
                    saveUserInfo(response)
                    startNextActivity(StartActivity::class.java)

                } else {
                    onFailureLogin(response)
                }

            } catch (e: Exception) {
                runOnUiThread {
                    Log.e(LandingActivity.TAG, "서버 통신 오류: ${e.message}")
                    showToast("로그인 요청 실패")
                }
            }
        }
    }

    private fun saveUserInfo(response: BaseResponse<LoginRes>) {
        userManager.saveUserIdx(response.data!!.userIdx)
        tokenManager.saveToken(response.data.tokenInfo)
    }

    private fun onFailureLogin(response: BaseResponse<LoginRes>) = with(binding) {
        runOnUiThread {
            when (response.code) {
                400 -> {
                    arrayOf(edittextEmail, editTextPassword).forEach { it.setBackgroundResource(R.drawable.textbox_state_error) }

                    textviewErrorMessage.apply {
                        visibility = View.VISIBLE
                        text = getString(R.string.login_incorrect)
                    }
                }

                500 -> showToast(getString(R.string.unexpected_error_occurred))
                else -> showToast(response.message)
            }
        }
    }

    private fun setFocusChangeListener() = with(binding) {
        arrayOf(edittextEmail, editTextPassword).forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                editText.setBackgroundResource(
                    if (hasFocus) R.drawable.textbox_state_focused
                    else R.drawable.textbox_state_normal
                )

                if (hasFocus) textviewErrorMessage.visibility = View.INVISIBLE
            }
        }
    }
}