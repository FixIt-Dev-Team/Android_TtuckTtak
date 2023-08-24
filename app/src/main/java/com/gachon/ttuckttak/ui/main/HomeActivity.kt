package com.gachon.ttuckttak.ui.main

import androidx.activity.viewModels
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityHomeBinding
import com.gachon.ttuckttak.ui.problem.ProblemCategoryActivity
import com.gachon.ttuckttak.ui.setting.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint

import com.gachon.ttuckttak.ui.main.HomeViewModel.NavigateTo.*

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate, TransitionMode.HORIZONTAL) {

    private val viewModel: HomeViewModel by viewModels()

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        setObservers()
    }

    private fun setObservers() = with(binding) {
        viewModel.diagnosis.observe(this@HomeActivity) {
            if (it != null) {
                textviewLatestResultText.text = it.context
                textviewLatestResultTime.text = it.date
            }
        }

        viewModel.viewEvent.observe(this@HomeActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is ProblemCategory -> {
                        startNextActivity(ProblemCategoryActivity::class.java)
                    }
                    is Settings -> {
                        startNextActivity(SettingsActivity::class.java)
                    }
                }
            }
        }
    }
}