package com.gachon.ttuckttak.ui.login

import android.util.Patterns
import android.view.View
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityLoginBinding

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

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