package com.gachon.ttuckttak.ui.terms

import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityTermsUseBinding
import com.gachon.ttuckttak.ui.join.JoinPart3Activity

class TermsUseActivity : BaseActivity<ActivityTermsUseBinding>(ActivityTermsUseBinding::inflate) {
    override fun initAfterBinding() = with(binding) {
       // 뒤로가기 버튼을 눌렀을 경우 -- JoinPart3Activity로 이동
        buttonBack.setOnClickListener {
            startNextActivity(JoinPart3Activity::class.java)
        }
    }
}