package com.gachon.ttuckttak.ui.solution

import android.content.Intent
import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionBypassDto
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
    private var bypassListI : Array<SolutionBypassDto>? = null

    @Inject lateinit var solutionService: SolutionService

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun initAfterBinding() = with(binding) {
        val solIdx = intent.getStringExtra("solIdx")
        val progress = intent.getIntExtra("progress", 0)
        if (intent.hasExtra("solutionBs")) {
            bypassListI = intent.getParcelableArrayExtra("solutionBs", SolutionBypassDto::class.java)
            Log.i(TAG, "bypass: ${bypassListI?.get(0)}")
            Log.i(TAG, "bypass name: ${bypassListI?.get(0)?.targetEntryName}")
            Log.i(TAG, "bypass start Idx: ${bypassListI?.get(0)?.startEntryIdx}")
            Log.i(TAG, "bypass target Idx: ${bypassListI?.get(0)?.targetEntryIdx}")
        }

        getSolDetail(solIdx!!, progress)
        setClickListener(solIdx, progress)
    }

    // Progress bar 표시 및 단계에 따라 완료 버튼 표시
    private fun setProgress(maxN: Int, nowN: Int) = with(binding) {
        Log.i(TAG, "progress: ${nowN + 1} of $maxN")
        val progressBar = arrayListOf<ArrayList<AppCompatImageView>>(
            arrayListOf(), // 0
            arrayListOf(), // 1
            arrayListOf(progress2First, progress2Second),
            arrayListOf(progress3First, progress3Second, progress3Third),
            arrayListOf(progress4First, progress4Second, progress4Third, progress4Fourth),
            arrayListOf(progress5First, progress5Second, progress5Third, progress5Fourth, progress5Fifth)
        )

        // 완료 여부 확인
        if (maxN == nowN + 1) {
            done = true
            buttonComplete.background = AppCompatResources.getDrawable(this@SolutionDescActivity, R.drawable.bg_rectangle_round)
            buttonComplete.text = getString(R.string.complete)
            buttonComplete.setTextColor(getColor(R.color.general_theme_white))
        }

        // Progress Bar
        if (maxN in 2..5) {
            for (bar in progressBar[maxN]) {
                bar.visibility = View.VISIBLE
            }
            for (i in 0..nowN) {
                progressBar[maxN][i].setColorFilter(getColor(R.color.main_theme_blue))
            }
        } else {
            Log.e(TAG, "progress step not in range (2,5)")
        }

    }

    // Bypass 리스트 (RecyclerView) 표시
    private fun setBypass(solutionBs : List<SolutionBypassDto>) = with(binding) {
        val adapter = BypassAdapter(solutionBs)

        bypassList.adapter = adapter
        bypassList.layoutManager = LinearLayoutManager(this@SolutionDescActivity, LinearLayoutManager.VERTICAL, false)

        adapter.setItemClickListener(object: BypassAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                // TODO("Case that returns to survey - not considered")
                val intent = Intent(this@SolutionDescActivity, SolutionActivity::class.java)
                intent.putExtra("entryID", solutionBs[position].targetEntryIdx)
                intent.putExtra("progress", 0)

                startActivity(intent)
            }
        })
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
                if (bypassListI != null) {
                    intent.putExtra("solutionBs", bypassListI)
                }
                startActivity(intent)
                finish()
            }
        }
    }

    // 서버로부터 Solution Detail API 수신
    // setProgress() 및 setBypass() 호출 => 스파게티 코드가 되지만 현재 API상 이렇게 구현하는 게 한계임
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

                        // 바이패스 설정
                        if (subcontents.size == progress + 1 && bypassListI != null) {
                            setBypass(bypassListI!!.toList())
                        }

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