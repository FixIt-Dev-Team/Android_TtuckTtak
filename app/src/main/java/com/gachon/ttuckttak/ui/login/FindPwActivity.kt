package com.gachon.ttuckttak.ui.login

import android.content.Intent
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
        buttonFindgen.setOnClickListener {
            val email = edittextFindEmail.text

            // 입력한 email 값 전달하기
            val intent = Intent(this@FindPwActivity, ResetPwActivity::class.java).apply {
                putExtra("email", edittextFindEmail.text.toString())

                // Todo: 서버에 로그인 요청

                // Todo: 결과에 따른 처리
                // ResetPwActivity로 이동
                startNextActivity(ResetPwActivity::class.java)
            }

            buttonBack.setOnClickListener {
                finish()
            }
        }
    }

    private fun setFocusChangeListener() = with(binding) {
        edittextFindEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                edittextFindEmail.error = null
            } else { // 포커스가 해제되었을 때
                val email = edittextFindEmail.text.toString()

                if (RegexUtil.isValidEmail(email) || email.isBlank()) { // 올바른 이메일 형식이거나 비어 있는 경우
                    edittextFindEmail.error = null
                } else { // 올바르지 않은 이메일 형식을 입력한 경우
                    edittextFindEmail.error = getString(R.string.invalid_email_format)
                }
            }
        }
    }
}