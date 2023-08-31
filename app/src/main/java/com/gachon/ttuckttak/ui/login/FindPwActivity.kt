package com.gachon.ttuckttak.ui.login

import android.util.Patterns
import android.view.View
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityFindPwBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

import com.gachon.ttuckttak.ui.login.FindPwViewmodel.NavigateTo.*

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

        viewModel.showErrorMessage.observe(this@FindPwActivity) { showErrorMessage ->
            when (showErrorMessage) {
                true -> showNonExistingAccountError()
                false -> null
            }
        }

        // 일회성 show toast
        lifecycleScope.launch {
            viewModel.showToastEvent.collect { message ->
                showToast(message)
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