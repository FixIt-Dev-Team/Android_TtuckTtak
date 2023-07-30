package com.gachon.ttuckttak.ui.join

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityJoinPart1Binding
import com.gachon.ttuckttak.utils.RegexUtil

class JoinPart1Activity : BaseActivity<ActivityJoinPart1Binding>(ActivityJoinPart1Binding::inflate) {
    override fun initAfterBinding() = with(binding) {
        // 뒤로가기 버튼을 눌렀을 경우
        buttonBack.setOnClickListener {
            finish()
        }

        // 올바른 email인 경우에만 인증코드 보내기 버튼 활성화
        edittextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { // 입력하기 전에 동작

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { // 입력 중 동작

            }

            override fun afterTextChanged(p0: Editable?) { // 입력 후 동작
                val email = p0.toString()
                buttonCertificationCode.isEnabled = RegexUtil.isValidEmail(email)
            }
        })

        // 인증코드 보내기 버튼을 눌렀을 경우
        buttonCertificationCode.setOnClickListener {
            // Todo: 서버에 이메일 인증번호 요청하고 intent에 같이 전송

            val intent = Intent(this@JoinPart1Activity, JoinPart2Activity::class.java)
            intent.putExtra("email", edittextEmail.text.toString()) // 입력한 email 값 전달하기
            startActivity(intent) // JoinPart2Activity로 이동
        }
    }
}