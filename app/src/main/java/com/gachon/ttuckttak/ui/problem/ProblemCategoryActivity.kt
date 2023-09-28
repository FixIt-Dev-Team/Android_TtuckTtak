package com.gachon.ttuckttak.ui.problem

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.view.MotionEvent
import android.view.View
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.SolutionManager
import com.gachon.ttuckttak.data.local.UserManager
import com.gachon.ttuckttak.databinding.ActivityProblemCategoryBinding
import com.gachon.ttuckttak.ui.solution.SolutionBeforeActivity
import javax.inject.Inject

class ProblemCategoryActivity : BaseActivity<ActivityProblemCategoryBinding>(ActivityProblemCategoryBinding::inflate, TransitionMode.HORIZONTAL) {

    @Inject lateinit var userManager: UserManager

    private var isLayoutVisible = false

    override fun initAfterBinding() {
        setClickListener()
        setTouchListner()
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
            finish()
        }

        buttonDisplayProb.setOnClickListener {
            val intent = Intent(this@ProblemCategoryActivity, SolutionBeforeActivity::class.java)

            intent.putExtra("surveyIdx", SolutionManager.DISPLAY)
            intent.putExtra("pattern", "1")

            startActivity(intent)
            finish()
        }

        buttonUseProb.setOnClickListener {
            val intent = Intent(this@ProblemCategoryActivity, ProblemYNActivity::class.java)

            intent.putExtra("surveyIdx", SolutionManager.USAGE)
            intent.putExtra("subtitle", R.string.usage_prob_1)
            intent.putExtra("pattern", "")

            startActivity(intent)
            finish()
        }

        buttonAccProb.setOnClickListener {
            val intent = Intent(this@ProblemCategoryActivity, ProblemYNActivity::class.java)

            intent.putExtra("surveyIdx", SolutionManager.ACC)
            intent.putExtra("subtitle", R.string.acc_prob_1)
            intent.putExtra("pattern", "")

            startActivity(intent)
            finish()
        }

        buttonAsk.setOnClickListener {
            // 문의 페이지 표시
            showLayout()
        }

        layoutCs.buttonConfirm.setOnClickListener {
            closeLayout()
        }
    }

    private fun setTouchListner() = with(binding) {
        layoutRoot.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                closeLayout()
                return@setOnTouchListener true
            }
            false
        }

        layoutCs.root.setOnTouchListener { _, _ -> true }

        layoutCs.buttonConfirm.setOnClickListener {
            copyInquiryEmailToClipboard() // 클립보드에 문의 이메일 복사
            closeLayout()
        }
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

    /**
     * 문의 이메일을 클립보드에 복사하는 Method
     */
    private fun copyInquiryEmailToClipboard() {
        val clipboardManager: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("label", "ttukttak@ttukttak.com") // 클립보드에 label이라는 이름표로 문의 이메일 저장
        clipboardManager.setPrimaryClip(clipData)
    }
}
