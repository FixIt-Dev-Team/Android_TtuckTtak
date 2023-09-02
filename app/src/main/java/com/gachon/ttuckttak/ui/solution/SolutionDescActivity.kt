package com.gachon.ttuckttak.ui.solution

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.AuthManager
import com.gachon.ttuckttak.data.remote.service.SolutionService
import com.gachon.ttuckttak.databinding.ActivitySolutionDescBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class SolutionDescActivity : BaseActivity<ActivitySolutionDescBinding>(ActivitySolutionDescBinding::inflate, TransitionMode.HORIZONTAL) {

    @Inject lateinit var authManager: AuthManager
    private var done = false

    @Inject lateinit var solutionService: SolutionService

    override fun initAfterBinding() = with(binding) {
        val solIdx = intent.getStringExtra("solIdx")
        val progress = intent.getIntExtra("progress", 0)

        getSolDetail(solIdx!!, progress)
        setClickListener(solIdx, progress)
    }

    private fun setProgress(maxN: Int, nowN: Int) = with(binding) {
        Log.i(TAG, "progress: ${nowN + 1} of $maxN")
        val progress2 = arrayListOf(progress2First, progress2Second)
        val progress3 = arrayListOf(progress3First, progress3Second, progress3Third)

        // 완료 여부 확인
        if (maxN == nowN + 1) {
            done = true
            buttonComplete.background = AppCompatResources.getDrawable(this@SolutionDescActivity, R.drawable.bg_rectangle_round)
            buttonComplete.text = getString(R.string.complete)
            buttonComplete.setTextColor(getColor(R.color.general_theme_white))
        }

        // TODO("Progress Bar")
        if (maxN == 2) {
            for (bar in progress2) {
                bar.visibility = View.VISIBLE
            }
            for (i in 0..nowN) {
                progress2[i].setColorFilter(getColor(R.color.main_theme_blue))
            }
        } else if (maxN == 3) {
            for (bar in progress3) {
                bar.visibility = View.VISIBLE
            }
            for (i in 0..nowN) {
                progress3[i].setColorFilter(getColor(R.color.main_theme_blue))
            }
        } else {
            Log.e(TAG, "Progress number doesn't match")
        }
    }

    private fun setClickListener(solIdx: String ,progress: Int) = with(binding) {
        buttonBack.setOnClickListener {
            finish()
        }

        buttonComplete.setOnClickListener {
            if (done) {
                finish()
            } else {
                val intent = Intent(this@SolutionDescActivity, SolutionDescActivity::class.java)
                intent.putExtra("solIdx", solIdx)
                intent.putExtra("progress", progress + 1)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun getSolDetail(solIdx: String, progress: Int) = with(binding) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = solutionService.getSolDetail(solIdx)

                withContext(Dispatchers.Main) {
                    if (response.isSuccess) {
                        val data = response.data!!

                        Log.i(TAG, "detailIdx: ${data.detailIdx}")
                        Log.i(TAG, "detailHeader: ${data.detailHeader}")
                        Log.i(TAG, "imageUrls: ${data.imageUrls}")
                        Log.i(TAG, "content: ${data.content}")
                        Log.i(TAG, "subContent: ${data.subContent}")

                        // 타이틀 설정
                        textviewTitle.text = data.content

                        // 콘텐트 설정
                        val subcontents: List<String> = data.subContent.split("\\n ")
                        textContent.text = data.detailHeader
                        textDescription.text = subcontents[progress]

                        // 프로그레스 바 설정
                        setProgress(subcontents.size, progress)

                        // 이미지 설정
                        Glide.with(this@SolutionDescActivity)
                            .load(data.imageUrls[progress])
                            .into(image)
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "서버 통신 오류: ${e.message}")
                    showToast("솔루션 디테일 통신 실패")
                }
            }
        }
    }

    companion object {
        const val TAG = "Solution Detail"
    }
}