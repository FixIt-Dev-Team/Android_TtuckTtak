package com.gachon.ttuckttak.ui.setting

import androidx.activity.viewModels
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivitySettingsBinding
import com.gachon.ttuckttak.ui.login.LandingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

import com.gachon.ttuckttak.ui.setting.SettingsViewmodel.NavigateTo.*

@AndroidEntryPoint
class SettingsActivity : BaseActivity<ActivitySettingsBinding>(
    ActivitySettingsBinding::inflate,
    TransitionMode.HORIZONTAL
) {

    private val viewModel: SettingsViewmodel by viewModels()

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        setObservers()
    }

    private fun setObservers() {
        viewModel.viewEvent.observe(this@SettingsActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Before -> finish()
                    is SettingsAlert -> startNextActivity(SettingsAlertActivity::class.java)
                    is SettingsProfile -> startNextActivity(SettingsProfileActivity::class.java)
                    is Landing -> startActivityWithClear(LandingActivity::class.java)
                }
            }
        }

        viewModel.profile.observe(this@SettingsActivity) { profile ->
            if (profile != null) {
                binding.textviewUserName.text = profile.userName
                binding.textviewUserEmail.text = profile.email

                if (profile.profileImgUrl.isEmpty()) { // 사용자의 프로필 이미지가 없는 경우 기본 이미지를 사용하고
                    binding.imageviewProfile.setImageDrawable(
                        AppCompatResources.getDrawable(
                            this@SettingsActivity,
                            R.drawable.img_profile
                        )
                    )

                } else { // 사용자의 프로필 이미지가 있는 경우 Glide를 이용해 프로필 이미지를 설정한다
                    Glide.with(this@SettingsActivity)
                        .load(viewModel.profile.value!!.profileImgUrl)
                        .into(binding.imageviewProfile)
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
}