package com.gachon.ttuckttak.ui.join

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.remote.service.AuthService
import com.gachon.ttuckttak.databinding.ActivityJoinPart2Binding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject
import kotlin.concurrent.timer

@AndroidEntryPoint
class JoinPart2Activity : BaseActivity<ActivityJoinPart2Binding>(ActivityJoinPart2Binding::inflate, TransitionMode.HORIZONTAL) {
    private var time = 300 // 5분
    private lateinit var timerTask: Timer

    private val email: String by lazy { intent.getStringExtra("email")!! }
    private lateinit var authCode: String

    @Inject lateinit var authService: AuthService

    private var isLayoutVisible = false // layout alert 화면이 현재 보여지고 있는지

    override fun initAfterBinding() = with(binding) {
        authCode = intent.getStringExtra("code")!!

        setUpEmailText()
        startTimer()
        setClickListener()
        setTouchListener()
        setTextChangeListener()
        setFocusChangeListener()
    }

    // 이메일 텍스트 설정
    private fun setUpEmailText() = with(binding) {
        textviewEmail.text = email
        layoutAlert.textviewReconfirmEmail.text = email
    }

    private fun setClickListener() = with(binding) {
        buttonBack.setOnClickListener { finish() }
        textviewCertificationCodeProblem.setOnClickListener { showLayout() }
        layoutAlert.buttonResend.setOnClickListener { handleResendButtonClick() }
        buttonCertification.setOnClickListener { handleAuthCodeVerification() }
    }

    // 인증 코드 재전송 버튼 처리
    private fun handleResendButtonClick() = with(binding) {
        resendAuthCode()
        resetTimer()

        // 인증코드 입력화면 초기화
        edittextCertificationCode.apply {
            text = null
            setBackgroundResource(R.drawable.textbox_state_normal)
        }
        textviewErrorMessage.visibility = View.INVISIBLE

        closeLayout()
    }


    private fun resendAuthCode() = lifecycleScope.launch(Dispatchers.IO) {
        try {
            val response = authService.emailConfirm(email)
            response.data?.code?.let { authCode = it } // null이 아닌 인증 코드를 새로 발급 받았을 때 update
            Log.i("response", response.toString())

        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Log.e("JoinPart2Activity", "서버 통신 오류: ${e.message}")
                showToast("이메일 인증 요청 실패")
            }
        }
    }

    private fun handleAuthCodeVerification() = with(binding) {
        if (edittextCertificationCode.text.toString() == authCode) { // 올바른 인증코드인 경우 email과 함께 다음 화면 실행
            Intent(this@JoinPart2Activity, JoinPart3Activity::class.java).apply {
                putExtra("email", email)
            }.also { startActivity(it) }

        } else { // 올바르지 않은 인증코드인 경우
            edittextCertificationCode.setBackgroundResource(R.drawable.textbox_state_error)
            textviewErrorMessage.apply {
                visibility = View.VISIBLE
                textviewErrorMessage.text = getString(R.string.error_code)
            }
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
        runOnUiThread { binding.textviewTimer.text = "${time / 60} : ${time % 60}" }
    }

    private fun handleTimerExpiration() = with(binding) {
        if (time == 0) {
            authCode = "Expired auth code" // 사용자가 입력하지 못하는 글자(8글자 이상)로 바꾸어 인증코드 만료 처리

            runOnUiThread {
                edittextCertificationCode.setBackgroundResource(R.drawable.textbox_state_error)
                textviewErrorMessage.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.run_out_code)
                }
            }

            timerTask.cancel()
        }
    }

    private fun setTouchListener() = with(binding) {
        // layout 밖을 클릭 했을 때 layout 내리기
        layoutRoot.setOnTouchListener { _, event -> closeLayoutOnTouchOutside(event) }

        // layout 내부를 클릭 했을 땐 그대로
        layoutAlert.root.setOnTouchListener { _, _ -> true }
    }

    // 레이아웃 밖을 터치했을 때 레이아웃 닫기
    private fun closeLayoutOnTouchOutside(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            closeLayout()
            return true
        }
        return false
    }

    // layout alert 화면 보여줄 때
    private fun showLayout() = with(binding.layoutAlert) {
        if (!isLayoutVisible) {
            root.visibility = View.VISIBLE
            root.translationY = root.height.toFloat()
            root.translationZ = Float.MAX_VALUE // 가장 큰 값을 줌으로써 인증하기 버튼 위로 나오게
            root.animate().translationY(0f).setDuration(300).start()
            isLayoutVisible = true
        }
    }

    // layout alert 화면 내릴 때
    private fun closeLayout() = with(binding.layoutAlert) {
        if (isLayoutVisible) {
            root.animate().translationY(root.height.toFloat()).setDuration(300).withEndAction {
                root.visibility = View.GONE
            }.start()
            isLayoutVisible = false
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