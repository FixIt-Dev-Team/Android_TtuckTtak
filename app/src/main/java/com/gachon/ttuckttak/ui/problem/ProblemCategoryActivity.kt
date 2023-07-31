package com.gachon.ttuckttak.ui.problem

import android.content.Intent
import android.os.Bundle
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityProblemCategoryBinding
import com.gachon.ttuckttak.ui.main.HomeActivity

class ProblemCategoryActivity : BaseActivity<ActivityProblemCategoryBinding>(ActivityProblemCategoryBinding::inflate) {

    override fun initAfterBinding() = with(binding) {
        // back button
        fieldButtonBack.setOnClickListener {
            val intent = Intent(this@ProblemCategoryActivity, HomeActivity::class.java)
            startActivity(intent)
        }

        // "전원에 문제가 있어요" 버튼
        // 클릭 시 필요한 string 정보들을 intent로 넘겨주면서 ProblemYN 화면 실행
        buttonPowerProb.setOnClickListener {
            val intent = Intent(this@ProblemCategoryActivity, ProblemYNActivity::class.java)
            intent.putExtra("category", getString(R.string.power))
            intent.putExtra("subtitle", getString(R.string.power_prob_1))
            // tmp text
            intent.putExtra("true_subtitle", "AB_true_subtitle")
            intent.putExtra("true_content_1", "AB_true_content_1")
            startActivity(intent)
        }

        buttonAsk.setOnClickListener {
            TODO()
        }
    }
}