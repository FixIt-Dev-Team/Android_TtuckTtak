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
        buttonSend.setOnClickListener { sendEmailConfirmationRequest() }
    }

    // 이메일 인증 코드 전송 요청
    private fun sendEmailConfirmationRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val email = binding.edittextEmail.text.toString()
                val response = TtukttakServer.emailConfirm(email) // 서버에 이메일 인증코드 전송 요청
                Log.i("response", response.toString())

                with(response) {
                    if (isSuccess) moveToJoinPart2Activity(email, data?.code)
                    else handleErrorResponse(code, message)
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    showToast("이메일 인증 요청 실패")
                }
            }
        }
    }

    // 서버의 오류 응답 처리
    private fun handleErrorResponse(code: Int, message: String) {
        when (code) {
            400, 409 -> updateErrorState(message)
            500 -> showToast(getString(R.string.unexpected_error_occurred))
        }
    }

    // 이메일과 인증 코드를 가지고 JoinPart2Activity로 이동
    private fun moveToJoinPart2Activity(email: String, code: String?) {
        Intent(this, JoinPart2Activity::class.java).apply {
            putExtra("email", email)
            code?.let { putExtra("code", it) } // 인증코드 값이 있는 경우 같이 전달 (null이 아닌 경우 전달)
        }.also { startActivity(it) }
    }

    private fun setFocusChangeListener() = with(binding) {
        // 포커스가 변경될 때마다 이메일 유효성 검사 및 UI 업데이트 수행
        // Todo: text가 변경될 때마다 이메일 유효성 검사 및 UI 업데이트 수행되도록 변경
        edittextEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) updateFocusState()
            else validateEmailAndSetUi(email = edittextEmail.text.toString())
        }
    }

    // 이메일 상태를 검증하고 UI 상태를 설정하는 함수
    private fun validateEmailAndSetUi(email: String) {
        when {
            email.isEmpty() -> updateNormalState(false) // 비어있는 경우
            email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() -> updateNormalState(true) // 비어있지 않고 이메일 형식이 맞는 경우
            else -> updateErrorState(getString(R.string.invalid_email_format))
        }
    }

    private fun updateFocusState() = with(binding) {
        edittextEmail.run {
            setBackgroundResource(R.drawable.textbox_state_focused)
            setTextColor(ContextCompat.getColor(this@JoinPart1Activity, R.color.general_theme_black))
        }
        textviewErrorMessage.visibility = View.INVISIBLE
        buttonSend.isEnabled = false
    }

    // 정상 상태 UI 갱신
    private fun updateNormalState(enable: Boolean) = with(binding) {
        edittextEmail.run {
            setBackgroundResource(R.drawable.textbox_state_normal)
            setTextColor(ContextCompat.getColor(this@JoinPart1Activity, R.color.general_theme_black))
        }
        textviewErrorMessage.visibility = View.INVISIBLE
        buttonSend.isEnabled = enable
    }

    // 에러 상태 UI 갱신
    private fun updateErrorState(errorMessage: String) = with(binding) {
        edittextEmail.run {
            setBackgroundResource(R.drawable.textbox_state_error)
            setTextColor(ContextCompat.getColor(this@JoinPart1Activity, R.color.general_theme_red))
        }
        textviewErrorMessage.run {
            text = errorMessage
            visibility = View.VISIBLE
        }
        buttonSend.isEnabled = false
    }
}