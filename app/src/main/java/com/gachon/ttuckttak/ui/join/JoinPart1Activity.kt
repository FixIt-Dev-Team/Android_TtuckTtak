package com.gachon.ttuckttak.ui.join

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
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
        setTextChangeListener()
    }

    private fun setClickListener() = with(binding) {
        // 뒤로가기 버튼을 눌렀을 경우
        imagebuttonBack.setOnClickListener {
            finish()
        }

        // 인증코드 보내기 버튼을 눌렀을 경우
        buttonSend.setOnClickListener { sendEmailConfirmationRequest() }
    }

    // 이메일 인증 코드 전송 요청. 해당 API에서만 indeterminate progressbar를 사용한 이유는 외부 API 사용으로 시간이 오래걸리기 때문
    private fun sendEmailConfirmationRequest() {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                runOnUiThread {
                    binding.progressbar.visibility = View.VISIBLE // 서버에 이메일 인증코드 전송 요청하는 동안 progress bar 보이게
                    binding.buttonSend.isClickable = false // 요청하는 동안 재요청 하지 못하게 클릭 막기
                }

                val response = TtukttakServer.emailConfirm(email = binding.edittextEmail.text.toString()) // 서버에 이메일 인증코드 전송 요청
                Log.i("response", response.toString())

                runOnUiThread {
                    binding.progressbar.visibility = View.INVISIBLE // 인증코드가 온 경우 progress bar 가리게
                    binding.buttonSend.isClickable = true
                }

                with(response) {
                    if (isSuccess) moveToJoinPart2Activity(data?.code)
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
    private fun moveToJoinPart2Activity(code: String?) {
        Intent(this, JoinPart2Activity::class.java).apply {
            putExtra("email", binding.edittextEmail.text.toString())
            code?.let { putExtra("code", it) } // 인증코드 값이 있는 경우 같이 전달 (null이 아닌 경우 전달)
        }.also { startActivity(it) }
    }

    private fun setFocusChangeListener() = with(binding) {
        // 포커스가 변경될 때마다 UI 업데이트 수행
        edittextEmail.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) updateFocusState()
            else validateEmailAndSetUi(email = edittextEmail.text.toString())
        }
    }

    private fun setTextChangeListener() = with(binding) {
        edittextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            override fun afterTextChanged(p0: Editable?) {
                val email = edittextEmail.text.toString()
                buttonSend.isEnabled = email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()
            }
        })
    }

    // 이메일 상태를 검증하고 UI 상태를 설정하는 함수
    private fun validateEmailAndSetUi(email: String) {
        when {
            email.isEmpty() -> updateNormalState() // 비어있는 경우
            email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches() -> updateNormalState() // 비어있지 않고 이메일 형식이 맞는 경우
            else -> updateErrorState(getString(R.string.invalid_email_format))
        }
    }

    private fun updateFocusState() = with(binding) {
        edittextEmail.apply {
            setBackgroundResource(R.drawable.textbox_state_focused)
            setTextColor(ContextCompat.getColor(this@JoinPart1Activity, R.color.general_theme_black))
        }
        textviewErrorMessage.visibility = View.INVISIBLE
        buttonSend.isEnabled = false
    }

    // 정상 상태 UI 갱신
    private fun updateNormalState() = with(binding) {
        edittextEmail.apply {
            setBackgroundResource(R.drawable.textbox_state_normal)
            setTextColor(ContextCompat.getColor(this@JoinPart1Activity, R.color.general_theme_black))
        }
        textviewErrorMessage.visibility = View.INVISIBLE
    }

    // 에러 상태 UI 갱신
    private fun updateErrorState(errorMessage: String) = with(binding) {
        edittextEmail.apply {
            setBackgroundResource(R.drawable.textbox_state_error)
            setTextColor(ContextCompat.getColor(this@JoinPart1Activity, R.color.general_theme_red))
        }
        textviewErrorMessage.apply {
            text = errorMessage
            visibility = View.VISIBLE
        }
        buttonSend.isEnabled = false
    }
}