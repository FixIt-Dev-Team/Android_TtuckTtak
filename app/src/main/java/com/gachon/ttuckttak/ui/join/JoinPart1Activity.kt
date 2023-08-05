package com.gachon.ttuckttak.ui.join

import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.databinding.ActivityJoinPart1Binding
import com.gachon.ttuckttak.ui.login.LandingActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JoinPart1Activity : BaseActivity<ActivityJoinPart1Binding>(ActivityJoinPart1Binding::inflate) {

    override fun initAfterBinding() {
        setClickListener()
        setFocusChangeListener()
    }

    private fun setClickListener() = with(binding) {
        // 뒤로가기 버튼을 눌렀을 경우
        imagebuttonBack.setOnClickListener {
            finish()
        }

        // 인증코드 보내기 버튼을 눌렀을 경우
        buttonSend.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    val email = edittextEmail.text.toString()

                    // 서버에 이메일 인증코드 전송 요청
                    val response = TtukttakServer.emailConfirm(email)
                    Log.i("response", response.toString())

                    with(response) {
                        if (isSuccess) { // 서버에서 이메일에 성공적으로 인증코드를 보낸 경우
                            moveToJoinPart2Activity(email, response.data?.code)
                        }

                        else { // 서버에서 해당 이메일에 인증코드를 보내지 않은 경우
                            if(code == 400 || code == 409) { // 이메일 형식에 맞지 않은 경우, 이미 사용중인 이메일인 경우
                                updateErrorState(message)
                            }

                            if(code == 500) { // 예상치 못한 에러
                                // Todo: 오류 처리
                            }
                        }
                    }

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e(LandingActivity.TAG, "서버 통신 오류: ${e.message}")
                        showToast("이메일 인증 요청 실패")
                    }
                }
            }
        }
    }

    // email과 인증코드를 넣어 JoinPart2Activity 화면 실행하는 method
    private fun moveToJoinPart2Activity(email: String, code: String?) {
        val intent = Intent(this, JoinPart2Activity::class.java).apply {
            putExtra("email", email)
            code?.let { putExtra("code", it) } // 인증코드 값이 있는 경우 같이 전달 (null이 아닌 경우 전달)
        }

        startActivity(intent)
    }

    private fun setFocusChangeListener() = with(binding) {
        edittextEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                updateNormalState(false)

            } else {
                validateEmailAndSetUi(email = edittextEmail.text.toString())
            }
        }
    }

    // 이메일 상태를 검증하고 UI 상태를 설정하는 함수
    private fun validateEmailAndSetUi(email: String) {
        if (email.isEmpty()) { // 비어있는 경우
            updateNormalState(false)

        } else if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) { // 이메일이 비어있지 않고 이메일 형식이 맞는 경우
            updateNormalState(true)

        } else {
            updateErrorState(getString(R.string.invalid_email_format))
        }
    }

    // EditText, 오류 메시지, 인증번호 전송 버튼을 업데이트하는 함수
    private fun updateNormalState(enable: Boolean) = with(binding) {
        edittextEmail.setBackgroundResource(R.drawable.textbox_state_normal) // EditText의 배경 리소스 설정
        edittextEmail.setTextColor(ContextCompat.getColor(this@JoinPart1Activity, R.color.general_theme_black))
        textviewErrorMessage.visibility = View.INVISIBLE
        buttonSend.isEnabled = enable
    }

    // EditText, 오류 메시지, 인증번호 전송 버튼을 업데이트하는 함수
    private fun updateErrorState(errorMessage: String) = with(binding) {
        edittextEmail.setBackgroundResource(R.drawable.textbox_state_error) // EditText의 배경 리소스 설정
        edittextEmail.setTextColor(ContextCompat.getColor(this@JoinPart1Activity, R.color.general_theme_red))
        textviewErrorMessage.text = errorMessage
        textviewErrorMessage.visibility = View.VISIBLE
        buttonSend.isEnabled = false
    }
}