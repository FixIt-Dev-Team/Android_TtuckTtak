package com.gachon.ttuckttak.ui.setting

import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivitySettingsAlertBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

import com.gachon.ttuckttak.ui.setting.SettingsAlertViewmodel.NavigateTo.*

@AndroidEntryPoint
class SettingsAlertActivity : BaseActivity<ActivitySettingsAlertBinding>(ActivitySettingsAlertBinding::inflate, TransitionMode.HORIZONTAL) {

    private val viewModel: SettingsAlertViewmodel by viewModels()

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        setObservers()
    }

    private fun setObservers() {
        viewModel.viewEvent.observe(this@SettingsAlertActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Before -> finish()
                }
            }
        }

        viewModel.pushStatus.observe(this@SettingsAlertActivity) { value ->
            binding.switchEventAndFunctionPush.isChecked = value
        }

        viewModel.nightPushStatus.observe(this@SettingsAlertActivity) { value ->
            binding.switchNightTimePushAlert.isChecked = value
        }

        // 일회성 show toast
        lifecycleScope.launch {
            viewModel.showToastEvent.collect { message ->
                showToast(message)
            }
        }
    }
}