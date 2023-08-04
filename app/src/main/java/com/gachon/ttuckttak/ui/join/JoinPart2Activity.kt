package com.gachon.ttuckttak.ui.join

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.databinding.ActivityJoinPart2Binding
import com.gachon.ttuckttak.ui.login.LandingActivity
import com.gachon.ttuckttak.utils.RegexUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.concurrent.timer

class JoinPart2Activity :
    BaseActivity<ActivityJoinPart2Binding>(ActivityJoinPart2Binding::inflate) {
    var time = 300
    var timerTask: Timer? = null

    private val email: String by lazy { intent.getStringExtra("email")!! }
    private lateinit var authCode: String

    override fun initAfterBinding() = with(binding) {
        // timer 시작
        startTimer()

        // 인증 코드 설정
        authCode = intent.getStringExtra("code")!!

        // 뒤로가기 버튼을 눌렀을 경우
        buttonBack.setOnClickListener {
            finish()
        }

        // textviewEmail 값을 위의 email 값으로 변경하기
        textviewEmail.setText(email)

        // 올바른 인증코드인 경우에만 인증코드 보내기 버튼 활성화
        edittextCertificationCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            } // 입력하기 전에 동작

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                edittextCertificationCode.setBackgroundResource(R.drawable.box_input_text)

            } // 입력 중 동작

            override fun afterTextChanged(p0: Editable?) { // 입력 후 동작
                val code = p0.toString()
                buttonCertification.isEnabled = (code.length == 8)
            }
        })

        // 인증번호 재전송 버튼을 눌렀을 경우
        layoutAlert.buttonResend.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // 서버에 이메일 인증코드 전송 요청
                    TtukttakServer.emailConfirm(email).data?.code?.let {
                        authCode = it
                    } // null이 아닌 인증 코드를 새로 발급 받았을 때 update
                    Log.i("code", authCode)

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e(LandingActivity.TAG, "서버 통신 오류: ${e.message}")
                        showToast("이메일 인증 요청 실패")
                    }
                }
            }

            layoutAlert.root.visibility = View.INVISIBLE
            buttonCertification.visibility = View.VISIBLE
        }

        // 인증하기 버튼을 클릭한 경우
        buttonCertification.setOnClickListener {
            if (edittextCertificationCode.text.toString().equals(authCode)) { // 인증 코드가 맞는 경우
                textviewErrorCode.visibility = View.INVISIBLE

                // 다음 화면으로 이동하며 email 넘겨주기
                val intent = Intent(this@JoinPart2Activity, JoinPart3Activity::class.java).apply {
                    putExtra("email", email)
                }

                startActivity(intent)
            }
            // 인증코드가 다른 경우
            else {
                edittextCertificationCode.setBackgroundResource(R.drawable.box_error_text)
                textviewErrorCode.visibility = View.VISIBLE

            }
        }
    }

    // timer 함수 구현
    fun startTimer() = with(binding) {
        timerTask = timer(period = 1000) {

            time--

            val min = time / 60
            val sec = time % 60

            runOnUiThread {
                textviewTimer.setText("${min} : ${sec}")
            }

            if (time == 0) {
                edittextCertificationCode.setBackgroundResource(R.drawable.box_error_text)
                textviewRunOutCode.visibility = View.VISIBLE
                timerTask!!.cancel()
            }
        }
    }
}