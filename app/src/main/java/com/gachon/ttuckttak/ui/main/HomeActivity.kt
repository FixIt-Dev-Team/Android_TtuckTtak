package com.gachon.ttuckttak.ui.main

import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityHomeBinding
import com.gachon.ttuckttak.ui.problem.ProblemCategoryActivity
import com.gachon.ttuckttak.ui.setting.SettingsActivity
import dagger.hilt.android.AndroidEntryPoint

import com.gachon.ttuckttak.ui.main.HomeViewModel.NavigateTo.*

@AndroidEntryPoint
class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate, TransitionMode.HORIZONTAL) {

    private val viewModel: HomeViewModel by viewModels()
    private var backPressedTime = 0L

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        setObservers()
        this@HomeActivity.onBackPressedDispatcher.addCallback(this@HomeActivity, onBackPressedCallback)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() > backPressedTime + 2000) {
                backPressedTime = System.currentTimeMillis()
                showToast("한 번 더 누르면 종료합니다.")

            } else if (System.currentTimeMillis() <= backPressedTime + 2000) {
                ActivityCompat.finishAffinity(this@HomeActivity)
                System.exit(0)
            }
        }
    }

    private fun setObservers() = with(binding) {
        viewModel.diagnosis.observe(this@HomeActivity) { diagnosis ->
            if (diagnosis != null) {
                textviewLatestResultText.text = diagnosis.context
                textviewLatestResultTime.text = diagnosis.date
            }
        }

        viewModel.viewEvent.observe(this@HomeActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is ProblemCategory -> startNextActivity(ProblemCategoryActivity::class.java)
                    is Settings -> startNextActivity(SettingsActivity::class.java)
                }
            }
        }
    }
}