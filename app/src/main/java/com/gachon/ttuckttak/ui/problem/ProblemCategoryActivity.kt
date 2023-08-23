package com.gachon.ttuckttak.ui.problem

import android.content.Intent
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.SolutionManager
import com.gachon.ttuckttak.databinding.ActivityProblemCategoryBinding
import com.gachon.ttuckttak.ui.solution.SolutionBeforeActivity

class ProblemCategoryActivity : BaseActivity<ActivityProblemCategoryBinding>(ActivityProblemCategoryBinding::inflate) {

    override fun initAfterBinding() = with(binding) {
        setClickListener()
    }

    private fun setClickListener() = with(binding) {
        // back button
        imagebuttonBack.setOnClickListener {
            finish()
        }

        // 전원 문제 버튼 버튼
        // 클릭 시 필요한 string 정보들을 intent로 넘겨주면서 ProblemYN 화면 실행
        buttonPowerProb.setOnClickListener {
            val intent = Intent(this@ProblemCategoryActivity, ProblemYNActivity::class.java)

            intent.putExtra("surveyIdx", SolutionManager.POWER)
            intent.putExtra("subtitle", R.string.power_prob_1)
            intent.putExtra("pattern", "")

            startActivity(intent)
        }

        buttonDisplayProb.setOnClickListener {
            val intent = Intent(this@ProblemCategoryActivity, SolutionBeforeActivity::class.java)

            intent.putExtra("surveyIdx", SolutionManager.DISPLAY)
            intent.putExtra("pattern", "1")

            startActivity(intent)
        }

        buttonUseProb.setOnClickListener {
            val intent = Intent(this@ProblemCategoryActivity, ProblemYNActivity::class.java)

            intent.putExtra("surveyIdx", SolutionManager.USAGE)
            intent.putExtra("subtitle", R.string.usage_prob_1)
            intent.putExtra("pattern", "")

            startActivity(intent)
        }

        buttonAccProb.setOnClickListener {
            val intent = Intent(this@ProblemCategoryActivity, ProblemYNActivity::class.java)

            intent.putExtra("surveyIdx", SolutionManager.ACC)
            intent.putExtra("subtitle", R.string.acc_prob_1)
            intent.putExtra("pattern", "")

            startActivity(intent)
        }

        buttonAsk.setOnClickListener {
            TODO("문의 페이지로 연결")
        }
    }
}