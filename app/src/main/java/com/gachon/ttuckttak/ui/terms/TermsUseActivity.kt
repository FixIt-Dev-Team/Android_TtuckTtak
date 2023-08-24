package com.gachon.ttuckttak.ui.terms

import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityTermsUseBinding

class TermsUseActivity : BaseActivity<ActivityTermsUseBinding>(ActivityTermsUseBinding::inflate, TransitionMode.HORIZONTAL) {
    override fun initAfterBinding() = with(binding) {
        // 뒤로가기 버튼을 눌렀을 경우
        buttonBack.setOnClickListener {
            finish()
        }
    }
}