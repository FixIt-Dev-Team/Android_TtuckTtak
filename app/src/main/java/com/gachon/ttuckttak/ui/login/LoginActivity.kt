package com.gachon.ttuckttak.ui.login

import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import android.util.Patterns
import android.view.View
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.LoginReq
import com.gachon.ttuckttak.databinding.ActivityLoginBinding
import com.gachon.ttuckttak.utils.RegexUtil
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
        buttonLogin.setOnClickListener {

        }

        buttonBack.setOnClickListener {
            finish()
        }

        edittextEmailAdd.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    edittextEmailAdd.setBackgroundResource(R.drawable.box_input_text)
                    textviewIncorrect.visibility = View.INVISIBLE
                    textviewNotAccount.visibility = View.INVISIBLE
                } else {
                    val email = edittextEmailAdd.text.toString()

                    if (RegexUtil.isValidEmail(email)) {
                        edittextEmailAdd.setBackgroundResource(R.drawable.box_white)
                    } else {
                        edittextEmailAdd.setBackgroundResource(R.drawable.box_error_text)
                    }
                }
            }
        })

        edittextPw.setOnFocusChangeListener(object : View.OnFocusChangeListener {
            override fun onFocusChange(view: View, hasFocus: Boolean) {
                if (hasFocus) {
                    edittextPw.setBackgroundResource(R.drawable.box_input_text)
                    textviewNotAccount.visibility = View.INVISIBLE
                    textviewIncorrect.visibility = View.INVISIBLE
                } else {
                    val pw = edittextPw.text.toString()

                    if (RegexUtil.isValidPw(pw)) {
                        edittextPw.setBackgroundResource(R.drawable.box_white)
                        buttonLogin.isEnabled = RegexUtil.isValidPw(pw)
                    } else {
                        edittextPw.setBackgroundResource(R.drawable.box_error_text)
                    }
                }
            }
        })

        buttonLogin.setOnClickListener {
            val email = edittextEmailAdd.text.toString()
            val pw = edittextPw.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // 서버에 이메일 인증코드 전송 요청
                    val response = TtukttakServer.login(LoginReq(email, pw))
                    Log.i("response", response.toString())
                    userManager.saveUserIdx(response.data!!.userIdx)
                    tokenManager.saveToken(response.data!!.tokenInfo)
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
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
            }

            else {
                val email = edittextEmail.text.toString()

                if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) { // 이메일 형식에 맞는 경우
                    edittextEmail.setBackgroundResource(R.drawable.textbox_state_normal)
                    textviewErrorMessage.visibility = View.INVISIBLE

                } else {
                    edittextEmail.setBackgroundResource(R.drawable.textbox_state_error)
                    textviewErrorMessage.visibility = View.VISIBLE
                    textviewErrorMessage.text = getString(R.string.wrong_email_or_password)
                }
            }
        }
    }
}