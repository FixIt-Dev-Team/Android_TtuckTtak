package com.gachon.ttuckttak.ui.login

import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityResetPwBinding

class ResetPwActivity : BaseActivity<ActivityResetPwBinding>(ActivityResetPwBinding::inflate) {
    // 입력한 email 받기
    private val email: String by lazy { intent.getStringExtra("email")!! }
    override fun initAfterBinding() = with(binding) {
        setUi()
        setClickListener()
    }

    private fun setUi() = with(binding) {
        textviewEmail.setText(email) // email text 변경
    }

    private fun setClickListener() = with(binding) {
        // Todo: 고객센터를 클릭했을 때

        // 닫기 버튼을 눌렀을 경우
        buttonClose.setOnClickListener {
            startNextActivity(LoginActivity::class.java)
        }
    }
}