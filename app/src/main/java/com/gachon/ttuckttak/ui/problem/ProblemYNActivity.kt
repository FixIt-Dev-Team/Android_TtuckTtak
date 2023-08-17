package com.gachon.ttuckttak.ui.problem

import android.content.Intent
import android.util.Log
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.SolutionManager
import com.gachon.ttuckttak.databinding.ActivityProblemYnBinding
import com.gachon.ttuckttak.ui.solution.SolutionBeforeActivity

class ProblemYNActivity : BaseActivity<ActivityProblemYnBinding>(ActivityProblemYnBinding::inflate) {

    private val solutionManager: SolutionManager by lazy { SolutionManager(this@ProblemYNActivity) }

    override fun initAfterBinding() = with(binding){
        val surveyIdx = intent.getIntExtra("surveyIdx", 0)
        val subtitleId = intent.getIntExtra("subtitle", -1)
        val resPattern = intent.getStringExtra("pattern")
        // val level = intent.getIntExtra("level", 0)

        // Title
        title.text = getString(R.string.title, getString(solutionManager.getCategory(surveyIdx)))

        // Subtitle
        subtitle.text = getString(subtitleId)

        // TODO("true/false 타이틀 등 정해지지 않음")

        // 뒤로가기
        imagebuttonBack.setOnClickListener {
            finish()
        }

        // 예
        buttonY.setOnClickListener {
            val pattern = resPattern + "1"

            if (solutionManager.isSolution(surveyIdx, pattern)) {
                val intent = Intent(this@ProblemYNActivity, SolutionBeforeActivity::class.java)

                intent.putExtra("surveyIdx", surveyIdx)
                intent.putExtra("pattern", solutionManager.convertPattern(surveyIdx, pattern))
                // TODO("level 어떻게 계산하는지 백엔드에 질문")
                // intent.putExtra("level", level + 1)

                startActivity(intent)

            } else if (solutionManager.isQuestion(surveyIdx, pattern)) {
                val intent = Intent(this@ProblemYNActivity, ProblemYNActivity::class.java)

                intent.putExtra("surveyIdx", surveyIdx)
                intent.putExtra("subtitle", solutionManager.getSubtitle(surveyIdx, pattern))
                intent.putExtra("pattern", pattern)
                // TODO("level 어떻게 계산하는지 백엔드에 질문")
                // intent.putExtra("level", level + 1)

                startActivity(intent)

            } else {
                TODO("Jump to CS")
            }
        }

        // 아니오
        buttonN.setOnClickListener {
            val pattern = resPattern + "0"

            if (solutionManager.isSolution(surveyIdx, pattern)) {
                val intent = Intent(this@ProblemYNActivity, SolutionBeforeActivity::class.java)

                intent.putExtra("surveyIdx", surveyIdx)
                intent.putExtra("pattern", solutionManager.convertPattern(surveyIdx, pattern))
                // TODO("level 어떻게 계산하는지 백엔드에 질문")
                // intent.putExtra("level", level + 1)

                startActivity(intent)

            } else if (solutionManager.isQuestion(surveyIdx, pattern)) {
                val intent = Intent(this@ProblemYNActivity, ProblemYNActivity::class.java)

                intent.putExtra("surveyIdx", surveyIdx)
                intent.putExtra("subtitle", solutionManager.getSubtitle(surveyIdx, pattern))
                intent.putExtra("pattern", pattern)
                // TODO("level 어떻게 계산하는지 백엔드에 질문")
                // intent.putExtra("level", level + 1)

                startActivity(intent)

            } else {
                TODO("Jump to CS")
            }
        }
    }

}