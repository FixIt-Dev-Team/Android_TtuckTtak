package com.gachon.ttuckttak.ui.login

import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityFindPwBinding
import com.gachon.ttuckttak.ui.login.FindPwViewmodel.NavigateTo.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FindPwActivity : BaseActivity<ActivityFindPwBinding>(ActivityFindPwBinding::inflate, TransitionMode.HORIZONTAL) {

    private val viewModel: FindPwViewmodel by viewModels()

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        setObservers()
        setFocusChangeListener()
    }

    private fun setObservers() {
        viewModel.viewEvent.observe(this@FindPwActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Before -> finish()
                    is ResetPw -> startNextActivity(ResetPwActivity::class.java)
                }
            }
        }

        lifecycleScope.launch {
            // 서버 응답 코드를 구독하여 UI 업데이트 로직을 결정
            viewModel.response.collect { response ->
                if (response?.code == 400) { // 에러코드가 400인 경우, 에러 메시지 표시 함수 호출
                    showNonExistingAccountError()
                }
            }
        }

        lifecycleScope.launch {
            // 토스트 메시지 내용을 구독하여 메시지를 보여준다
            viewModel.showToastEvent.collect { message ->
                if (message != null) {
                    showToast(message)
                }
            }
        }
    }

    private fun showNonExistingAccountError() {
        with(binding) {
            textviewErrorMessage.visibility = View.VISIBLE
            edittextEmail.apply {
                setBackgroundResource(R.drawable.textbox_state_error)
                setTextColor(ContextCompat.getColor(this@FindPwActivity, R.color.general_theme_red))
            }
        }
    }

    private fun setFocusChangeListener() = with(binding) {
        edittextEmail.setOnFocusChangeListener { _, hasFocus ->
            val email = edittextEmail.text.toString()

            if (hasFocus) {
                textviewErrorMessage.visibility = View.INVISIBLE
                edittextEmail.apply {
                    setBackgroundResource(R.drawable.textbox_state_focused)
                    setTextColor(ContextCompat.getColor(this@FindPwActivity, R.color.general_theme_black))
                }

            } else {
                edittextEmail.setBackgroundResource(R.drawable.textbox_state_normal)
                buttonSend.isEnabled = Patterns.EMAIL_ADDRESS.matcher(email).matches() // email 양식에 맞는다면 전송 버튼 활성화
            }
        }
    }
}