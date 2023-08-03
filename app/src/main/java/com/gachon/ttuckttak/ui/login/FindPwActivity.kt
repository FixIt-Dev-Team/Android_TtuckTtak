package com.gachon.ttuckttak.ui.login

import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityFindPwBinding
import com.gachon.ttuckttak.utils.RegexUtil

class FindPwActivity : BaseActivity<ActivityFindPwBinding>(ActivityFindPwBinding::inflate) {

    override fun initAfterBinding() {
        setClickListener()
        setFocusChangeListener()
    }

    private fun setClickListener() = with(binding) {
        buttonSend.setOnClickListener {

        }

        imagebuttonBack.setOnClickListener {
            finish()
        }


    }

    private fun setFocusChangeListener() = with(binding) {
        editTextEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                editTextEmail.error = null
            }

            else { // 포커스가 해제되었을 때
                val email = editTextEmail.text.toString()

                if (RegexUtil.isValidEmail(email) || email.isBlank()) { // 올바른 이메일 형식이거나 비어 있는 경우
                    textinputlayoutEmail.error = null
                } else { // 올바르지 않은 이메일 형식을 입력한 경우
                    textinputlayoutEmail.error = getString(R.string.invalid_email_format)
                }
            }
        }
    }
}