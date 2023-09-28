package com.gachon.ttuckttak.ui.terms

import androidx.activity.viewModels
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityTermsPromoteBinding

import com.gachon.ttuckttak.ui.terms.TermsPromoteViewmodel.NavigateTo.*

class TermsPromoteActivity : BaseActivity<ActivityTermsPromoteBinding>(ActivityTermsPromoteBinding::inflate, TransitionMode.HORIZONTAL) {

    private val viewModel: TermsPromoteViewmodel by viewModels()

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        setObservers()
    }

    private fun setObservers() {
        viewModel.viewEvent.observe(this@TermsPromoteActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Back -> finish()
                }
            }
        }
    }
}