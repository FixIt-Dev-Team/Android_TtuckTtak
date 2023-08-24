package com.gachon.ttuckttak.ui.main

import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityStartBinding

class StartActivity : BaseActivity<ActivityStartBinding>(ActivityStartBinding::inflate, TransitionMode.HORIZONTAL) {

    override fun initAfterBinding() = with(binding) {
        buttonStart.setOnClickListener {
            startNextActivity(HomeActivity::class.java)
        }
    }
}