package com.gachon.ttuckttak.ui.login

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityFindPwBinding
import com.gachon.ttuckttak.utils.RegexUtil

class FindPwActivity : BaseActivity<ActivityFindPwBinding>(ActivityFindPwBinding::inflate) {

    override fun initAfterBinding() {
        setClickListener()
        setTextChangeListener()
    }

    private fun setClickListener() = with(binding) {
        buttonFindgen.setOnClickListener {
            val email = edittextFindEmail.text.toString()

            // Todo: 서버에 계정 찾기 요청

            // Todo: 결과에 따른 처리

            // ResetPwActivity로 이동
            val intent = Intent(this@FindPwActivity, ResetPwActivity::class.java).apply {
                putExtra("email", email) // 입력한 email 값 전달하기
            }

            startActivity(intent)
        }

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun setTextChangeListener() = with(binding) {
        // 올바른 email인 경우에만 계정 찾기 버튼 활성화
        edittextFindEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {} // 입력하기 전에 동작

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {} // 입력 중 동작

            override fun afterTextChanged(p0: Editable?) { // 입력 후 동작
                val email = p0.toString()
                buttonFindgen.isEnabled = RegexUtil.isValidEmail(email)
            }
        })
    }
}