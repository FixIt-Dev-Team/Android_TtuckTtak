package com.gachon.ttuckttak.ui.join

import android.view.View
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityJoinPart2Binding
import java.util.*
import kotlin.concurrent.timer

class JoinPart2Activity : BaseActivity<ActivityJoinPart2Binding>(ActivityJoinPart2Binding::inflate) {
    var time = 0
    var timerTask : Timer? = null

    override fun initAfterBinding() = with(binding) {
        // timer 시작
        startTimer()

        // 인증하기 버튼 비활성화
        buttonCertification.setClickable(false)

        // 뒤로가기 버튼을 눌렀을 경우
        buttonBack.setOnClickListener {
            // 이전 화면으로 이동
            startActivityWithClear(JoinPart1Activity::class.java)
        }

        // JoinPart1Activity에서 입력받았던 email 값 받기
        val email = intent.getStringExtra("email")
        // textviewEmail 값을 위의 email 값으로 변경하기
        textviewEmail.setText(email)
        // email textbox 테두리 색 변경하기
        textviewEmail.setBackgroundResource(R.drawable.box_input_text)

        // 인증코드를 눌렀을 경우 -- textbox 색 변경하기
        edittextCertificationCode.setBackgroundResource(R.drawable.box_input_text)

        // 인증번호에 문제가 생겼을 경우 -- 해당 버튼 클릭
        textviewCertificationCodeProblem.setOnClickListener {
            // 인증하기 버튼 invisible
            buttonCertification.visibility = View.INVISIBLE
            // 아래에 있던 레이아웃 visible
            layoutAlert.root.visibility = View.VISIBLE

            // 인증번호 재전송 버튼을 눌렀을 경우 -- 임시설정
            layoutAlert.buttonResend.setOnClickListener {
                // layoutAlert invisible 처리 및 인증하기 버튼 visible
                layoutAlert.root.visibility = View.INVISIBLE
                buttonCertification.visibility = View.VISIBLE
            }
        }

        // 인증코드 확인 여부 -- 임시설정
        // 인증코드가 일치하는 경우
        /*if(true) {
            // 인증코드가 일치할 경우 -- textbox 색 변경하기
            layoutCertificationCode.setBackgroundResource(R.drawable.box_input_text)

            // 인증하기 버튼 활성화
            buttonCertification.setClickable(true)
            // 인증하기 버튼 색 변경
            buttonCertification.setBackgroundColor(ContextCompat.getColor(applicationContext, R.color.main_theme_blue))
            // 인증하기 버튼 글자 색 변경
            buttonCertification.setTextColor(getColor(R.color.white))

            // 인증하기 버튼을 눌렀을 경우
            buttonCertification.setOnClickListener {
                // JoinPart3Activity로 화면 전환
                startNextActivity(JoinPart3Activity::class.java)
            }
        }
        // 인증코드가 불일치하는 경우
        else {
            // 인증코드 입력 textbox 색 변경
            layoutCertificationCode.setBackgroundResource(R.drawable.box_error_text)

            // 잘못된 코드 입력 시 -- error code visible
            textviewErrorCode.visibility = View.VISIBLE
        }*/
    }

    // timer 함수 구현 -- Q.멀르겟숴여
    fun startTimer() = with(binding) {
       timerTask = timer(period = 10) {
           time++

           val min = time / 100
           val sec = time % 60

           runOnUiThread {
               textviewTimer.setText("${min} : ${sec}")
           }
       }
    }
}