package com.gachon.ttuckttak.ui.terms

import androidx.activity.viewModels
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityTermsUseBinding

import com.gachon.ttuckttak.ui.terms.TermsUseViewmodel.NavigateTo.*

class TermsUseActivity : BaseActivity<ActivityTermsUseBinding>(ActivityTermsUseBinding::inflate, TransitionMode.HORIZONTAL) {

    private val viewModel: TermsUseViewmodel by viewModels()

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        setObservers()
    }

    private fun setObservers() {
        viewModel.viewEvent.observe(this@TermsUseActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Back -> finish()
                }
            }
        }
    }
}