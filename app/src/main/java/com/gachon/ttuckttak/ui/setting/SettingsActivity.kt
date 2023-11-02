package com.gachon.ttuckttak.ui.setting

import android.content.ClipData
import android.content.ClipboardManager
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivitySettingsBinding
import com.gachon.ttuckttak.ui.login.LandingActivity
import com.gachon.ttuckttak.ui.setting.SettingsViewmodel.NavigateTo.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SettingsActivity : BaseActivity<ActivitySettingsBinding>(
    ActivitySettingsBinding::inflate,
    TransitionMode.HORIZONTAL
) {

    private val viewModel: SettingsViewmodel by viewModels()
    private var isLayoutVisible = false

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this // LiveData 관찰을 위한 lifecycleOwner 설정
        setObservers()
        setClickListener()
        setTouchListener()
    }

    private fun setClickListener() = with(binding) {
        buttonConsumerCenter.setOnClickListener {
            showLayout()
        }
    }

    private fun showLayout() = with(binding.layoutCs) {
        if (!isLayoutVisible) {
            root.visibility = View.VISIBLE
            root.translationY = root.height.toFloat()
            root.translationZ = Float.MAX_VALUE // 가장 큰 값을 줌으로써 인증하기 버튼 위로 나오게
            root.animate().translationY(0f).setDuration(300).start()
            isLayoutVisible = true
        }

        buttonConfirm.setOnClickListener {
            emailCopy()
            closeLayout()
        }
    }

    private fun emailCopy() = with(binding.layoutCs) {
        val email = textviewEmail.text.toString()

        val clipboardManager: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        //클립보드에 EMAIL이라는 이름표로 email 값을 복사하여 저장
        val clipData = ClipData.newPlainText("EMAIL", email)

        clipboardManager.setPrimaryClip(clipData)

        //복사가 되었다면 토스트메시지 노출
        showToast("복사되었습니다.")
    }

    private fun closeLayout() = with(binding.layoutCs) {
        if (isLayoutVisible) {
            root.animate().translationY(root.height.toFloat()).setDuration(300).withEndAction {
                root.visibility = View.GONE
            }.start()
            isLayoutVisible = false
        }
    }

    private fun setTouchListener() = with(binding) {
        // layout 밖을 클릭 했을 때 layout 내리기
        onboarding.setOnTouchListener { _, event -> closeLayoutOnTouchOutside(event) }

        // layout 내부를 클릭 했을 땐 그대로
        layoutCs.root.setOnTouchListener { _, _ -> true }
    }

    // 레이아웃 밖을 터치했을 때 레이아웃 닫기
    private fun closeLayoutOnTouchOutside(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            closeLayout()
            return true
        }
        return false
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

        // 일회성 show toast
        lifecycleScope.launch {
            viewModel.showToastEvent.collect { message ->
                showToast(message)
            }
        }
    }
}