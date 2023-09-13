package com.gachon.ttuckttak.ui.problem

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.view.View
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.SolutionManager
import com.gachon.ttuckttak.databinding.ActivityProblemYnBinding
import com.gachon.ttuckttak.ui.solution.SolutionBeforeActivity

class ProblemYNActivity : BaseActivity<ActivityProblemYnBinding>(ActivityProblemYnBinding::inflate, TransitionMode.HORIZONTAL) {

    private val solutionManager: SolutionManager by lazy { SolutionManager(this@ProblemYNActivity) }

    private var isLayoutVisible = false

    override fun initAfterBinding() = with(binding){
        val surveyIdx = intent.getIntExtra("surveyIdx", 0)
        val subtitleId = intent.getIntExtra("subtitle", -1)
        val resPattern = intent.getStringExtra("pattern")

        setUI(surveyIdx, subtitleId)
        setClickListener(resPattern!!, surveyIdx)
        setTouchListner()
    }

    private fun setUI(surveyIdx: Int, subtitleId: Int) = with(binding) {
        // Log
        Log.i("YN", "surveyIdx: $surveyIdx")
        Log.i("YN", "subtitleID: $subtitleId")

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

    }

    private fun setClickListener(resPattern: String, surveyIdx: Int) = with(binding) {
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

            } else if (solutionManager.isCS(surveyIdx, pattern)) {
                shadow.visibility = View.VISIBLE
                showLayout()
            } else {
                TODO("Check Bypass")
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

            }  else if (solutionManager.isCS(surveyIdx, pattern)) {
                shadow.visibility = View.VISIBLE
                showLayout()
            } else {
                TODO("Check Bypass")
            }
        }

        layoutCs.buttonConfirm.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("label", "ttukttak@ttukttak.com")
            clipboard.setPrimaryClip(clipData)

            showToast("복사되었습니다.") // 복사가 되었다면 토스트메시지 노출
            finish()
        }
    }

    private fun setTouchListner() = with(binding) {
        layoutRoot.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                shadow.visibility = View.INVISIBLE
                closeLayout()
                return@setOnTouchListener true
            }
            false
        }

        layoutCs.root.setOnTouchListener { _, _ -> true }
    }

    // CS 화면 표시
    private fun showLayout() = with(binding.layoutCs) {
        if (!isLayoutVisible) {
            root.visibility = View.VISIBLE
            root.translationY = root.height.toFloat()
            root.translationZ = Float.MAX_VALUE // 가장 큰 값을 줌으로써 인증하기 버튼 위로 나오게
            root.animate().translationY(0f).setDuration(300).start()
            isLayoutVisible = true
        }
    }

    // CS 화면 닫기
    private fun closeLayout() = with(binding.layoutCs) {
        if (isLayoutVisible) {
            root.animate().translationY(root.height.toFloat()).setDuration(300).withEndAction {
                root.visibility = View.GONE
            }.start()
            isLayoutVisible = false
        }
    }

}