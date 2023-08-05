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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.concurrent.timer

class JoinPart2Activity : BaseActivity<ActivityJoinPart2Binding>(ActivityJoinPart2Binding::inflate) {
    private var time = 300 // 5분
    private lateinit var timerTask: Timer

    private val email: String by lazy { intent.getStringExtra("email")!! }
    private lateinit var authCode: String

    override fun initAfterBinding() = with(binding) {
        // 인가코드 설정
        authCode = intent.getStringExtra("code")!!

        // 이메일 설정
        textviewEmail.text = email // textviewEmail 값을 위의 email 값으로 변경하기
        layoutAlert.textviewReconfirmEmail.text = email

        // 기능 설정
        startTimer() // timer 시작
        setClickListener()
        setTextChangeListener()
        setFocusChangeListener()
    }

    private fun setClickListener() = with(binding) {
        // 뒤로가기 버튼을 눌렀을 경우
        buttonBack.setOnClickListener {
            finish()
        }

        // 인증코드에 문제가 있나요? 텍스트를 눌렀을 경우
        textviewCertificationCodeProblem.setOnClickListener {
            // Todo: show popup with 위로 올라오는 애니메이션
            layoutAlert.root.visibility = View.VISIBLE
            buttonCertification.visibility = View.INVISIBLE
        }

        // 인증번호 재전송 버튼을 눌렀을 경우
        layoutAlert.buttonResend.setOnClickListener {
            resendAuthCode()
            resetTimer()

            // 인증코드 입력화면 초기화
            edittextCertificationCode.text = null // 입력한 인증코드 지우기
            edittextCertificationCode.setBackgroundResource(R.drawable.textbox_state_normal)
            textviewErrorMessage.visibility = View.INVISIBLE

            // Todo: hide popup with 아래로 내려가는 애니메이션
            layoutAlert.root.visibility = View.INVISIBLE
            buttonCertification.visibility = View.VISIBLE
        }

        // 인증하기 버튼을 클릭한 경우
        buttonCertification.setOnClickListener {
            handleAuthCodeVerification()
        }
    }

    private fun resendAuthCode() = lifecycleScope.launch(Dispatchers.IO) {
        try {
            // 서버에 이메일 인증코드 전송 요청
            val response = TtukttakServer.emailConfirm(email)

            // null이 아닌 인증 코드를 새로 발급 받았을 때 update
            response.data?.code?.let { authCode = it }
            Log.i("code", authCode)

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e(LandingActivity.TAG, "서버 통신 오류: ${e.message}")
                showToast("이메일 인증 요청 실패")
            }
        }
    }

    private fun handleAuthCodeVerification() = with(binding) {
        if (edittextCertificationCode.text.toString() == authCode) { // 올바른 인증코드인 경우
            val intent = Intent(this@JoinPart2Activity, JoinPart3Activity::class.java).apply {
                putExtra("email", email)
            }

            startActivity(intent)

        } else { // 올바르지 않은 인증코드인 경우
            edittextCertificationCode.setBackgroundResource(R.drawable.textbox_state_error)
            textviewErrorMessage.visibility = View.VISIBLE
            textviewErrorMessage.text = getString(R.string.error_code)
        }
    }

    // timer 함수 구현
    private fun startTimer() {
        timerTask = timer(period = 1000) {
            time--
            updateTimerText()
            handleTimerExpiration()
        }
    }

    private fun resetTimer() = with(binding) {
        timerTask.cancel() // 기존에 실행 중인 타이머 취소
        time = 300 // 5분 재설정
        startTimer()
    }

    private fun updateTimerText() {
        runOnUiThread {
            binding.textviewTimer.text = "${time / 60} : ${time % 60}"
        }
    }

    private fun handleTimerExpiration() = with(binding) {
        if (time == 0) {
            authCode = "Expired auth code" // 사용자가 입력하지 못하는 글자(8글자 이상)로 바꾸어 인증코드 만료 처리

            runOnUiThread {
                edittextCertificationCode.setBackgroundResource(R.drawable.textbox_state_error)
                textviewErrorMessage.visibility = View.VISIBLE
                textviewErrorMessage.text = getString(R.string.run_out_code)
            }
            timerTask.cancel()
        }
    }

    private fun setTextChangeListener() = with(binding) {
        edittextCertificationCode.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                buttonCertification.isEnabled = (p0.toString().length == 8)
            }
        })
    }

    private fun setFocusChangeListener() = with(binding) {
        edittextCertificationCode.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                edittextCertificationCode.setBackgroundResource(R.drawable.textbox_state_focused) // EditText의 배경 리소스 설정
                textviewErrorMessage.visibility = View.INVISIBLE

            } else {
                edittextCertificationCode.setBackgroundResource(R.drawable.textbox_state_normal) // EditText의 배경 리소스 설정
            }
        }
    }
}