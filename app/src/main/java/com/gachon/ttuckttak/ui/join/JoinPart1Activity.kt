package com.gachon.ttuckttak.ui.join

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.databinding.ActivityJoinPart1Binding
import com.gachon.ttuckttak.ui.login.LandingActivity
import com.gachon.ttuckttak.utils.RegexUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JoinPart1Activity : BaseActivity<ActivityJoinPart1Binding>(ActivityJoinPart1Binding::inflate) {
    override fun initAfterBinding() = with(binding) {
        // 뒤로가기 버튼을 눌렀을 경우
        imagebuttonBack.setOnClickListener {
            finish()
        }

        // 올바른 email인 경우에만 인증코드 보내기 버튼 활성화
        edittextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { } // 입력하기 전에 동작

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) { } // 입력 중 동작

            override fun afterTextChanged(p0: Editable?) { // 입력 후 동작
                val email = p0.toString()
                buttonSend.isEnabled = RegexUtil.isValidEmail(email)
            }
        })

        // 인증코드 보내기 버튼을 눌렀을 경우
        buttonSend.setOnClickListener {
            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // 서버에 이메일 인증코드 전송 요청
                    val response = TtukttakServer.emailConfirm(edittextEmail.text.toString())
                    Log.i("response", response.toString())

                    // email과 인증코드를 넣어 다음 화면 실행
                    val intent = Intent(this@JoinPart1Activity, JoinPart2Activity::class.java).apply {
                        putExtra("email", edittextEmail.text.toString()) // 입력한 email 값 전달하기
                        response.data?.code?.let { putExtra("code", it) } // 인증코드 값이 있는 경우 같이 전달 (null이 아닌 경우 전달)
                    }

                    startActivity(intent) // JoinPart2Activity로 이동

                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        Log.e(LandingActivity.TAG, "서버 통신 오류: ${e.message}")
                        showToast("이메일 인증 요청 실패")
                    }
                }
            }
        }
    }
}