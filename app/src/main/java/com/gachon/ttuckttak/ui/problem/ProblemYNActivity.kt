package com.gachon.ttuckttak.ui.problem

import android.content.Intent
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.SolutionManager
import com.gachon.ttuckttak.databinding.ActivityProblemYnBinding
import com.gachon.ttuckttak.ui.solution.SolutionBeforeActivity

class ProblemYNActivity : BaseActivity<ActivityProblemYnBinding>(ActivityProblemYnBinding::inflate, TransitionMode.HORIZONTAL) {

    private val solutionManager: SolutionManager by lazy { SolutionManager(this@ProblemYNActivity) }

    override fun initAfterBinding() = with(binding){
        val surveyIdx = intent.getIntExtra("surveyIdx", 0)
        val subtitleId = intent.getIntExtra("subtitle", -1)
        val resPattern = intent.getStringExtra("pattern")

        // Title
        title.text = getString(R.string.title, getString(solutionManager.getCategory(surveyIdx)))

        // Subtitle
        subtitle.text = getString(subtitleId)

        // content
        val contentTexts = solutionManager.getContent(subtitleId)!!
        trueSubtitle.text = getString(contentTexts[0])
        trueContent1.text = getString(contentTexts[1])
        trueContent2.text = getString(contentTexts[2])
        trueContent3.text = getString(contentTexts[3])
        falseSubtitle.text = getString(contentTexts[4])
        falseContent1.text = getString(contentTexts[5])
        falseContent2.text = getString(contentTexts[6])
        falseContent3.text = getString(contentTexts[7])

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
                intent.putExtra("level", 1)

                startActivity(intent)
                finish()

            } else if (solutionManager.isQuestion(surveyIdx, pattern)) {
                val intent = Intent(this@ProblemYNActivity, ProblemYNActivity::class.java)

                intent.putExtra("surveyIdx", surveyIdx)
                intent.putExtra("subtitle", solutionManager.getSubtitle(surveyIdx, pattern))
                intent.putExtra("pattern", pattern)

                startActivity(intent)
                finish()

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
                intent.putExtra("level", 1)

                startActivity(intent)
                finish()

            } else if (solutionManager.isQuestion(surveyIdx, pattern)) {
                val intent = Intent(this@ProblemYNActivity, ProblemYNActivity::class.java)

                intent.putExtra("surveyIdx", surveyIdx)
                intent.putExtra("subtitle", solutionManager.getSubtitle(surveyIdx, pattern))
                intent.putExtra("pattern", pattern)
                startActivity(intent)
                finish()

            } else {
                TODO("Jump to CS")
            }
        }
    }

}