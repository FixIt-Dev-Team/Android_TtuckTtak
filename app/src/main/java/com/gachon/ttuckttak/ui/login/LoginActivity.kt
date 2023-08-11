package com.gachon.ttuckttak.ui.login

import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.LoginReq
import com.gachon.ttuckttak.databinding.ActivityLoginBinding
import com.gachon.ttuckttak.ui.main.StartActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    private val userManager: UserManager by lazy { UserManager(this@LoginActivity) }
    private val tokenManager: TokenManager by lazy { TokenManager(this@LoginActivity) }

    override fun initAfterBinding() {
        setClickListener()
        setFocusChangeListener()
    }

    private fun setClickListener() = with(binding) {
        buttonBack.setOnClickListener {
            finish()
        }

        buttonLogin.setOnClickListener {
            val email = edittextEmail.text.toString()
            val pw = editTextPassword.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // 서버에 이메일 인증코드 전송 요청
                    val response = TtukttakServer.login(LoginReq(email, pw))
                    Log.i("response", response.toString())

                    if (response.isSuccess) {
                        userManager.saveUserIdx(response.data!!.userIdx)
                        tokenManager.saveToken(response.data.tokenInfo)
                        Log.i("test", tokenManager.getRefreshToken()!!)

                        startNextActivity(StartActivity::class.java)

                    } else {
                        withContext(Dispatchers.Main) {
                            when (response.code) {
                                400 -> {
                                    // 아이디나 비밀번호가 다른 경우
                                    edittextEmail.setBackgroundResource(R.drawable.textbox_state_error)
                                    editTextPassword.setBackgroundResource(R.drawable.textbox_state_error)
                                    textviewErrorMessage.visibility = View.VISIBLE
                                    textviewErrorMessage.text = getString(R.string.login_incorrect)
                                }

                                500 -> {
                                    showToast(getString(R.string.unexpected_error_occurred))
                                }
                            }
                        }

                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        Log.e(LandingActivity.TAG, "서버 통신 오류: ${e.message}")
                        showToast("로그인 요청 실패")
                    }
                }
            }

        }

        textFindIdOrPw.setOnClickListener {
            startNextActivity(FindPwActivity::class.java)
        }
    }
    private fun setFocusChangeListener() = with(binding) {
        edittextEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                edittextEmail.setBackgroundResource(R.drawable.textbox_state_focused)
                textviewErrorMessage.visibility = View.INVISIBLE

            } else {
                edittextEmail.setBackgroundResource(R.drawable.textbox_state_normal)
            }
        }

        editTextPassword.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                editTextPassword.setBackgroundResource(R.drawable.textbox_state_focused)
                textviewErrorMessage.visibility = View.INVISIBLE

            } else {
                editTextPassword.setBackgroundResource(R.drawable.textbox_state_normal)
            }
        }
    }
}