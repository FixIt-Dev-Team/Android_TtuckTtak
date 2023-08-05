package com.gachon.ttuckttak.ui.join

import android.content.Intent
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
    private var time = 300
    private var timerTask: Timer? = null

    private val email: String by lazy { intent.getStringExtra("email")!! }
    private lateinit var authCode: String

    override fun initAfterBinding() = with(binding) {
        authCode = intent.getStringExtra("code")!!
        textviewEmail.text = email // textviewEmail 값을 위의 email 값으로 변경하기
        startTimer() // timer 시작
        setClickListener()
    }

    private fun setClickListener() = with(binding) {
        // 뒤로가기 버튼을 눌렀을 경우
        buttonBack.setOnClickListener {
            finish()
        }

        // 인증번호 재전송 버튼을 눌렀을 경우
        layoutAlert.buttonResend.setOnClickListener {
            resendAuthCode()

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

    private fun updateTimerText() {
        runOnUiThread {
            binding.textviewTimer.text = "${time / 60} : ${time % 60}"
        }
    }

    private fun handleTimerExpiration() = with(binding) {
        if (time == 0) {
            runOnUiThread {
                edittextCertificationCode.setBackgroundResource(R.drawable.textbox_state_error)
                textviewErrorMessage.visibility = View.VISIBLE
                textviewErrorMessage.text = getString(R.string.run_out_code)
            }
            timerTask!!.cancel()
        }
    }
}