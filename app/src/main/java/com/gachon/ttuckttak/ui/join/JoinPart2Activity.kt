package com.gachon.ttuckttak.ui.join

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.databinding.ActivityJoinPart2Binding
import com.gachon.ttuckttak.ui.login.StartActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.concurrent.timer

class JoinPart2Activity : BaseActivity<ActivityJoinPart2Binding>(ActivityJoinPart2Binding::inflate) {
    var time = 0
    var timerTask: Timer? = null

    private val email: String by lazy { intent.getStringExtra("email")!! }
    private lateinit var code: String

    override fun initAfterBinding() = with(binding) {
        // timer 시작
        startTimer()

        // 인증 코드 설정
        code = intent.getStringExtra("code")!!

        // 뒤로가기 버튼을 눌렀을 경우
        buttonBack.setOnClickListener {
            finish()
        }

        // textviewEmail 값을 위의 email 값으로 변경하기
        textviewEmail.setText(email)

        // 인증코드를 눌렀을 경우 -- textbox 색 변경하기
        edittextCertificationCode.setBackgroundResource(R.drawable.box_input_text)

        // 인증번호에 문제가 생겼을 경우 -- 해당 버튼 클릭
        textviewCertificationCodeProblem.setOnClickListener {
            buttonCertification.visibility = View.INVISIBLE
            layoutAlert.root.visibility = View.VISIBLE
        }

        // 인증번호 재전송 버튼을 눌렀을 경우
        layoutAlert.buttonResend.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // 서버에 이메일 인증코드 전송 요청
                    TtukttakServer.emailConfirm(email).data?.code?.let { code = it } // null이 아닌 인증 코드를 새로 발급 받았을 때 update
                    Log.i("code", code)

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e(StartActivity.TAG, "서버 통신 오류: ${e.message}")
                        showToast("이메일 인증 요청 실패")
                    }
                }
            }

            layoutAlert.root.visibility = View.INVISIBLE
            buttonCertification.visibility = View.VISIBLE
        }

        // 인증하기 버튼을 클릭한 경우
        buttonCertification.setOnClickListener {
            if (edittextCertificationCode.text.toString().equals(code)) { // 인증 코드가 맞는 경우
                // 다음 화면으로 이동하며 email 넘겨주기
                val intent = Intent(this@JoinPart2Activity, JoinPart3Activity::class.java).apply {
                    putExtra("email", email)
                }

                startActivity(intent)

            } else { // 인증코드가 맞지 않는 경우
                showToast("올바르지 않은 인증 코드입니다.")
            }
        }

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