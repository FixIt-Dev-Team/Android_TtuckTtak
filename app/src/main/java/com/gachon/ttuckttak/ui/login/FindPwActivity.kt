package com.gachon.ttuckttak.ui.login

import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.databinding.ActivityFindPwBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindPwActivity : BaseActivity<ActivityFindPwBinding>(ActivityFindPwBinding::inflate) {

    override fun initAfterBinding() {
        setClickListener()
        setFocusChangeListener()
    }

    private fun setClickListener() = with(binding) {
        buttonSend.setOnClickListener {
            val email = edittextEmail.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // 서버에 비밀번호 변경 전송 요청
                    val response = TtukttakServer.changePw(email)
                    Log.i("response", response.toString())

                    if (response.isSuccess) {
                        // ResetPwActivity로 이동
                        val intent = Intent(this@FindPwActivity, ResetPwActivity::class.java).apply {
                            putExtra("email", email) // 입력한 email 값 전달하기
                        }

                        startActivity(intent)

                    } else {
                        withContext(Dispatchers.Main) {
                            when (response.code) {
                                400 -> {
                                    // 계정이 존재하지 않는 경우
                                    textviewErrorMessage.visibility = View.VISIBLE
                                    edittextEmail.setBackgroundResource(R.drawable.textbox_state_error)
                                    edittextEmail.setTextColor(ContextCompat.getColor(this@FindPwActivity, R.color.general_theme_red))
                                }
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

        imagebuttonBack.setOnClickListener {
            finish()
        }
    }

    private fun setFocusChangeListener() = with(binding) {
        edittextEmail.setOnFocusChangeListener { _, hasFocus ->
            val email = edittextEmail.text.toString()

            if (hasFocus) {
                textviewErrorMessage.visibility = View.INVISIBLE
                edittextEmail.setBackgroundResource(R.drawable.textbox_state_focused)
                edittextEmail.setTextColor(ContextCompat.getColor(this@FindPwActivity, R.color.general_theme_black))

            } else {
                edittextEmail.setBackgroundResource(R.drawable.textbox_state_normal)
                buttonSend.isEnabled = Patterns.EMAIL_ADDRESS.matcher(email).matches() // email 양식에 맞는다면 전송 버튼 활성화
            }
        }
    }
}