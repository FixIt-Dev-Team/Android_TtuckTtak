package com.gachon.ttuckttak.ui.join

import android.content.Intent
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityJoinPart1Binding

class JoinPart1Activity : BaseActivity<ActivityJoinPart1Binding>(ActivityJoinPart1Binding::inflate) {
    override fun initAfterBinding() = with(binding) {
        // 인증코드 보내기 버튼 비활성화
        buttonCertificationCode.setClickable(false)

        // 뒤로가기 버튼을 눌렀을 경우
        buttonBack.setOnClickListener {
            finish()
        }

        // 이메일 입력창을 클릭했을 경우
        edittextEmail.setOnClickListener {
            // edittextEmail 값 가져오기
            val email = edittextEmail.text.toString()

            // textbox 테두리 색 변경
            edittextEmail.setBackgroundResource(R.drawable.box_input_text)

            // email이 중복일 경우 -- 임시설정
            /*if(true) {
                // textbox 테두리 및 글자 색 변경
                layoutJoinEmail.setBackgroundResource(R.drawable.box_error_text)
                edittextEmail.setTextColor(ContextCompat.getColor(applicationContext, R.color.general_theme_error))

                // invisible 되어 있던 error message 보이기
                textviewOverlapEmail.visibility = View.VISIBLE
            }
            // email이 중복이 아닐 경우
            else {
                // error message invisible하기
                textviewOverlapEmail.visibility = View.INVISIBLE

                // email이 적합할 경우 -- textbox 및 글자 색 변경하기
                layoutJoinEmail.setBackgroundResource(R.drawable.box_input_text)
                edittextEmail.setTextColor(ContextCompat.getColor(applicationContext, R.color.black))

                // 인증코드 보내기 버튼 색 변경
                buttonCertificationCode.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.main_theme_blue))
                // 인증코드 보내기 버튼 글자 색 변경
                buttonCertificationCode.setTextColor(getColor(R.color.white))
                // 인증코드 보내기 버튼 활성화
                buttonCertificationCode.setClickable(true)
            }*/
        }

        // 인증코드 보내기 버튼을 눌렀을 경우
        buttonCertificationCode.setOnClickListener {
            val intent = Intent(this@JoinPart1Activity, JoinPart2Activity::class.java)
            intent.putExtra("email", edittextEmail.text.toString()) // 입력한 email 값 전달하기
            startActivity(intent) // JoinPart2Activity로 이동
        }
    }
}