package com.gachon.ttuckttak.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityHomeBinding
import com.gachon.ttuckttak.ui.problem.ProblemCategoryActivity
import com.gachon.ttuckttak.ui.problem.ProblemYNActivity

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {

    override fun initAfterBinding() = with(binding) {
        imgButtonStart.setOnClickListener {
            val intent = Intent(this@HomeActivity, ProblemCategoryActivity::class.java)
            startActivity(intent)
        }
    }
}