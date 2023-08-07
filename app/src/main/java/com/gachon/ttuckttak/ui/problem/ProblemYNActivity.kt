package com.gachon.ttuckttak.ui.problem

import android.content.Intent
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityProblemYnBinding

class ProblemYNActivity : BaseActivity<ActivityProblemYnBinding>(ActivityProblemYnBinding::inflate) {

    override fun initAfterBinding() = with(binding){
        // back button
        imagebuttonBack.setOnClickListener {
            val intent = Intent(this@ProblemYNActivity, ProblemCategoryActivity::class.java)
            startActivity(intent)
        }

        // Title
        val category = intent.getStringExtra("category")
        title.text = getString(R.string.title, category)

        // Subtitle
        val subtitleText = intent.getStringExtra("subtitle")
        subtitle.text = subtitleText

        // True Subtitle
        val tSubtitle = intent.getStringExtra("true_subtitle")
        trueSubtitle.text = tSubtitle

        // True Content
        val tContent1 = intent.getStringExtra("true_content_1")
        trueContent1.text = tContent1
    }

}