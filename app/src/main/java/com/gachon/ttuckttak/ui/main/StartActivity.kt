package com.gachon.ttuckttak.ui.main

import androidx.activity.viewModels
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityStartBinding

import com.gachon.ttuckttak.ui.main.StartViewmodel.NavigateTo.*

class StartActivity : BaseActivity<ActivityStartBinding>(ActivityStartBinding::inflate, TransitionMode.HORIZONTAL) {

    private val viewModel: StartViewmodel by viewModels()

    override fun initAfterBinding() = with(binding) {
        binding.viewmodel = viewModel
        setObservers()
    }

    private fun setObservers() {
        viewModel.viewEvent.observe(this@StartActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Home -> startNextActivity(HomeActivity::class.java)
                }
            }
        }
    }
}