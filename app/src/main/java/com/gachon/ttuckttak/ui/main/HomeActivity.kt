package com.gachon.ttuckttak.ui.main

import android.content.Intent
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityHomeBinding
import com.gachon.ttuckttak.ui.problem.ProblemCategoryActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    override fun initAfterBinding() = with(binding) {
        imagebuttonStart.setOnClickListener {
            val intent = Intent(this@HomeActivity, ProblemCategoryActivity::class.java)
            startActivity(intent)
        }
    }
}