package com.gachon.ttuckttak.ui.login

import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.base.BaseResponse
import com.gachon.ttuckttak.data.remote.dto.auth.LoginRes
import com.gachon.ttuckttak.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

import com.gachon.ttuckttak.ui.login.LoginViewmodel.State.*
import com.gachon.ttuckttak.ui.login.LoginViewmodel.NavigateTo.*
import com.gachon.ttuckttak.ui.main.StartActivity

@AndroidEntryPoint
class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate, TransitionMode.VERTICAL) {

    private val viewModel: LoginViewmodel by viewModels()

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        setObservers()
        setFocusChangeListener()
    }

    private fun setObservers() {
        viewModel.state.observe(this@LoginActivity) { state ->
            when (state) {
                Error -> onFailureLogin(viewModel.response.value!!)
                else -> null
            }
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

    private fun onFailureLogin(response: BaseResponse<LoginRes>) = with(binding) {
        when (response.code) {
            400 -> {
                arrayOf(edittextEmail, editTextPassword).forEach { it.setBackgroundResource(R.drawable.textbox_state_error) }

                textviewErrorMessage.apply {
                    visibility = View.VISIBLE
                    text = getString(R.string.login_incorrect)
                }
            }
            else -> null
        }
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