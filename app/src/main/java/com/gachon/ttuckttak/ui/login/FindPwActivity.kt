package com.gachon.ttuckttak.ui.login

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.databinding.ActivityFindPwBinding
import com.gachon.ttuckttak.utils.RegexUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FindPwActivity : BaseActivity<ActivityFindPwBinding>(ActivityFindPwBinding::inflate) {

    override fun initAfterBinding() {
        setClickListener()
        setTextChangeListener()
    }

    private fun setClickListener() = with(binding) {
        buttonFindgen.setOnClickListener {
            val email = edittextFindEmail.text.toString()

            lifecycleScope.launch(Dispatchers.IO) {
                try {
                    // 서버에 비밀번호 변경 전송 요청
                    val response = TtukttakServer.changePw(email)
                    Log.i("response", response.toString())

                    if(response.isSuccess) {
                        // ResetPwActivity로 이동
                        val intent = Intent(this@FindPwActivity, ResetPwActivity::class.java).apply {
                            putExtra("email", email) // 입력한 email 값 전달하기
                        }

                        startActivity(intent)
                    } else {
                        runOnUiThread {
                            showToast(response.message)
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

        buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun setTextChangeListener() = with(binding) {
        // 올바른 email인 경우에만 계정 찾기 버튼 활성화
        edittextFindEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {} // 입력하기 전에 동작

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {} // 입력 중 동작

            override fun afterTextChanged(p0: Editable?) { // 입력 후 동작
                val email = p0.toString()
                buttonFindgen.isEnabled = RegexUtil.isValidEmail(email)
            }
        })
    }
}