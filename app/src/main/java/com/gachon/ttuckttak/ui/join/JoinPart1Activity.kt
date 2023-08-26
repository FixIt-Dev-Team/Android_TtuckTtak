package com.gachon.ttuckttak.ui.join

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.auth.EmailConfirmRes
import com.gachon.ttuckttak.databinding.ActivityJoinPart1Binding
import dagger.hilt.android.AndroidEntryPoint

import com.gachon.ttuckttak.ui.join.JoinPart1Viewmodel.State.*
import com.gachon.ttuckttak.ui.join.JoinPart1Viewmodel.NavigateTo.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class JoinPart1Activity : BaseActivity<ActivityJoinPart1Binding>(ActivityJoinPart1Binding::inflate, TransitionMode.HORIZONTAL) {

    private val viewModel: JoinPart1Viewmodel by viewModels()

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        setFocusChangeListener()
        setTextChangeListener()
        setObservers()
    }

    private fun setObservers() {
        viewModel.state.observe(this@JoinPart1Activity) { state ->
            when (state) {
                Loading -> showLoadingState(true)
                Success -> showSuccessState()
                Error -> showErrorState()
            }
        }

        viewModel.viewEvent.observe(this@JoinPart1Activity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Before -> finish()
                    is JoinPart2 -> {
                        val intent = Intent(this, JoinPart2Activity::class.java).apply {
                            putExtra("email", binding.edittextEmail.text.toString())
                            putExtra("code", viewModel.response.value!!.data!!.code)
                        }

                        startActivity(intent)
                    }
                }
            }
        }

        // 일회성 show toast
        lifecycleScope.launch {
            viewModel.showToastEvent.collect { message ->
                showToast(message)
            }
        }
     }

    private fun showLoadingState(isLoading: Boolean) {
        binding.progressbar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
        binding.buttonSend.isClickable = !isLoading
    }

    private fun showSuccessState() {
        showLoadingState(false)
        updateNormalState()
    }

    private fun showErrorState() {
        showLoadingState(false)
        handleErrorResponse(viewModel.response.value!!)
    }

    // 서버의 오류 응답 처리
    private fun handleErrorResponse(response: BaseResponse<EmailConfirmRes>) = with(response) {
        when (code) {
            400, 409 -> updateErrorState(message)
        }
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