package com.gachon.ttuckttak.ui.login

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityLoginBinding
import com.gachon.ttuckttak.ui.login.LoginViewmodel.NavigateTo.*
import com.gachon.ttuckttak.ui.main.StartActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate, TransitionMode.VERTICAL) {

    private val viewModel: LoginViewmodel by viewModels()

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        setObservers()
        setFocusChangeListener()
    }

    private fun setObservers() {
        viewModel.loginFail.observe(this@LoginActivity) {
            updateLoginFailUi()
        }

        viewModel.viewEvent.observe(this@LoginActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Before -> finish()
                    is Start -> startNextActivity(StartActivity::class.java)
                    is FindPw -> startNextActivity(FindPwActivity::class.java)
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

    private fun updateLoginFailUi() = with(binding) {
        arrayOf(edittextEmail, editTextPassword).forEach { edittext ->
            edittext.setBackgroundResource(R.drawable.textbox_state_error)
        }

        textviewErrorMessage.visibility = View.VISIBLE
    }

    private fun setFocusChangeListener() = with(binding) {
        arrayOf(edittextEmail, editTextPassword).forEach { editText ->
            editText.setOnFocusChangeListener { _, hasFocus ->
                editText.setBackgroundResource(R.drawable.textbox)

                if (hasFocus) textviewErrorMessage.visibility = View.INVISIBLE
            }
        }
    }
}