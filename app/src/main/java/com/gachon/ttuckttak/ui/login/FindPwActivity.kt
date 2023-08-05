package com.gachon.ttuckttak.ui.login

import android.content.Intent
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityFindPwBinding

class FindPwActivity : BaseActivity<ActivityFindPwBinding>(ActivityFindPwBinding::inflate) {

    override fun initAfterBinding() {
        setClickListener()
    }

    private fun setClickListener() = with(binding) {
        buttonSend.setOnClickListener {
            val email = edittextFindEmail.text.toString()

            // Todo: 서버에 계정 찾기 요청

            // Todo: 결과에 따른 처리

            // ResetPwActivity로 이동
            val intent = Intent(this@FindPwActivity, ResetPwActivity::class.java).apply {
                putExtra("email", email) // 입력한 email 값 전달하기
            }

            startActivity(intent)
        }

        imagebuttonBack.setOnClickListener {
            finish()
        }
    }
}