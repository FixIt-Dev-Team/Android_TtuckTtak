package com.gachon.ttuckttak.ui.main

import android.content.Intent
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityHomeBinding
import com.gachon.ttuckttak.ui.problem.ProblemCategoryActivity
import com.gachon.ttuckttak.ui.setting.SettingsActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    override fun initAfterBinding() = with(binding) {
        imgButtonStart.setOnClickListener {
            val intent = Intent(this@HomeActivity, ProblemCategoryActivity::class.java)
            startActivity(intent)
        }

        buttonSetting.setOnClickListener {
            startNextActivity(SettingsActivity::class.java)
        }
    }
}