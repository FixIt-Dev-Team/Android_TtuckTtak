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
    }

    private fun setClickListener() = with(binding) {
        buttonSend.setOnClickListener {
            val email = edittextEmail.text.toString()

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

        imagebuttonBack.setOnClickListener {
            finish()
        }
    }
}