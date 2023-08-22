package com.gachon.ttuckttak.ui.solution

import android.content.Intent
import android.media.session.MediaSession.Token
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.content.res.AppCompatResources
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.databinding.ActivitySolutionDescBinding
import com.gachon.ttuckttak.ui.main.HomeActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SolutionDescActivity : BaseActivity<ActivitySolutionDescBinding>(ActivitySolutionDescBinding::inflate) {

    private val tokenManager : TokenManager by lazy { TokenManager(this@SolutionDescActivity) }
    private var done = false

    override fun initAfterBinding() = with(binding) {
        val title = intent.getStringExtra("solTitle")
        val solIdx = intent.getStringExtra("solIdx")
        val progress = intent.getIntExtra("progress", 0)

        textviewTitle.text = title

        getSolDetail(solIdx!!, progress)
        setClickListener(title!!, solIdx, progress)
    }

    private fun setProgress(maxN: Int, nowN: Int) = with(binding) {
        Log.i(TAG, "progress: ${nowN + 1} of $maxN")
        if (maxN == nowN + 1) {
            done = true
            buttonComplete.text = getString(R.string.complete)
        }
        // TODO("Progress Bar")
    }

    private fun setClickListener(title: String, solIdx: String ,progress: Int) = with(binding) {
        buttonBack.setOnClickListener {
            finish()
        }

        buttonComplete.setOnClickListener {
            if (done) {
                finish()

                // TODO("완료 시 어떻게 동작할지?")
            } else {
                val intent = Intent(this@SolutionDescActivity, SolutionDescActivity::class.java)
                intent.putExtra("solTitle", title)
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
                val response = TtukttakServer.getSolDetail(solIdx, tokenManager.getAccessToken()!!)

                withContext(Dispatchers.Main) {
                    if (response.isSuccess) {
                        val data = response.data!!

                        Log.i(TAG, "detailIdx: ${data.detailIdx}")
                        Log.i(TAG, "detailHeader: ${data.detailHeader}")
                        Log.i(TAG, "imageUrls: ${data.imageUrls}")
                        Log.i(TAG, "content: ${data.content}")
                        Log.i(TAG, "subContent: ${data.subContent}")

                        // 타이틀 설정
                        textContent.text = data.detailHeader

                        // 콘텐트 설정
                        val contents: List<String> = data.content.split("\\n")
                        textDescription.text = contents[progress]

                        // 프로그레스 바 설정
                        setProgress(contents.size, progress)

                        // 이미지 설정
                        if (data.imageUrls.isEmpty()){
                            Log.i(SolutionBeforeActivity.TAG, "이미지 로딩 실패")
                        } else {
                            Glide.with(this@SolutionDescActivity)
                                .load(data.imageUrls[0])
                                .into(image)
                        }
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