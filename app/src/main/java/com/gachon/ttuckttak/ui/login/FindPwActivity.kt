package com.gachon.ttuckttak.ui.login

import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityFindPwBinding

class FindPwActivity : BaseActivity<ActivityFindPwBinding>(ActivityFindPwBinding::inflate) {

    override fun initAfterBinding() {
        setClickListener()
    }

    private fun setClickListener() = with(binding) {
        buttonSend.setOnClickListener {

        }

        imagebuttonBack.setOnClickListener {
            finish()
        }
    }
}