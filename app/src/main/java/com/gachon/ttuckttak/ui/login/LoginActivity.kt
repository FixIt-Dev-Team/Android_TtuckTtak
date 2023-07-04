package com.gachon.ttuckttak.ui.login

import androidx.core.widget.doOnTextChanged
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityLoginBinding
import com.gachon.ttuckttak.utils.RegexUtil

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    override fun initAfterBinding() {
        setClickListener()
        setFocusChangeListener()
        setTextChangeListener()
    }

    private fun setClickListener() = with(binding) {
        buttonLogin.setOnClickListener {
            val email = emailEditText.text
            val pw = pwEditText.text

            // Todo: 서버에 로그인 요청

            // Todo: 결과에 따른 처리
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun setFocusChangeListener() = with(binding) {
        emailEditText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                emailInputLayout.error = null
            }

            else { // 포커스가 해제되었을 때
                val email = emailEditText.text.toString()

                if (RegexUtil.isValidEmail(email) || email.isBlank()) { // 올바른 이메일 형식이거나 비어 있는 경우
                    emailInputLayout.error = null
                } else { // 올바르지 않은 이메일 형식을 입력한 경우
                    emailInputLayout.error = getString(R.string.invalid_email_format)
                }
            }
        }
    }

    private fun setTextChangeListener() = with(binding) {
        pwEditText.doOnTextChanged { pw, _, _, _ ->
            if (RegexUtil.isValidPw(pw.toString()) || pw.isNullOrBlank()) {
                pwInputLayout.error = null
            } else {
                pwInputLayout.error = getString(R.string.invalid_pw_format)
            }
        }
    }
}