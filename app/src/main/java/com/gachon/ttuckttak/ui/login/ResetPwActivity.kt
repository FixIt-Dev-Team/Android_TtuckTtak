package com.gachon.ttuckttak.ui.login

import androidx.activity.viewModels
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityResetPwBinding

import com.gachon.ttuckttak.ui.login.ResetPwViewmodel.NavigateTo.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPwActivity : BaseActivity<ActivityResetPwBinding>(
    ActivityResetPwBinding::inflate,
    TransitionMode.HORIZONTAL
) {

    private val viewModel: ResetPwViewmodel by viewModels()

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        setObservers()
    }

    private fun setObservers() {
        viewModel.viewEvent.observe(this@ResetPwActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Landing -> startActivityWithClear(LandingActivity::class.java)
                }
            }
        }
    }
}