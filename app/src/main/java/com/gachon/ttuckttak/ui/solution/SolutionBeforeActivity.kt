package com.gachon.ttuckttak.ui.solution

import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.SolutionManager
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionBypassDto
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionDto
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionEntryReq
import com.gachon.ttuckttak.data.remote.service.SolutionService
import com.gachon.ttuckttak.databinding.ActivitySolutionBeforeBinding
import com.gachon.ttuckttak.utils.DetailAdapter
import com.gachon.ttuckttak.utils.SolutionBeforeAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class SolutionBeforeActivity : BaseActivity<ActivitySolutionBeforeBinding>(ActivitySolutionBeforeBinding::inflate, TransitionMode.HORIZONTAL) {

    private val tokenManager: TokenManager by lazy { TokenManager(this@SolutionBeforeActivity) }
    private val solutionManager: SolutionManager by lazy { SolutionManager(this@SolutionBeforeActivity) }

    private var solutions: List<SolutionDto>? = null
    private var solutionPs: MutableList<String>? = mutableListOf()
    private var solutionBs: MutableList<SolutionBypassDto>? = null

    private var isLayoutVisible = false

    @Inject lateinit var solutionService: SolutionService

    override fun initAfterBinding() = with(binding) {
        val surveyIdx = intent.getIntExtra("surveyIdx", 0)
        val resPattern = intent.getStringExtra("pattern")!!.toInt(2)
        val level = intent.getIntExtra("level", 1)

        Log.i(TAG, "surveyIdx: $surveyIdx")
        Log.i(TAG, "resPattern: $resPattern")
        Log.i(TAG, "level: $level")

        getSolution(surveyIdx, resPattern, level)
        setClickListener(surveyIdx, resPattern, level)
        setTouchListener()
    }

    private fun setClickListener(surveyIdx: Int, pattern: Int, level: Int) = with(binding) {
        fieldButtonBack.setOnClickListener {
            finish()
        }

        fieldButtonMenu.setOnClickListener {
            TODO()
        }

        buttonFindSolution.setOnClickListener {
            val intent = Intent(this@SolutionBeforeActivity, SolutionActivity::class.java)

            intent.putExtra("surveyIdx", surveyIdx)
            intent.putExtra("pattern", pattern)
            intent.putExtra("level", level + 1)
            startActivity(intent)
            finish()
        }

        buttonComplete.setOnClickListener {
            finish()
        }
    }

    private fun solutionBtn() = with(binding) {
        val adapter = SolutionBeforeAdapter(solutions!!)
        buttonListView.adapter = adapter
        buttonListView.layoutManager = LinearLayoutManager(this@SolutionBeforeActivity)

        adapter.itemClick = object : SolutionBeforeAdapter.ItemClick {
            // 솔루션 버튼 클릭 시 디테일 레이아웃 표시
            override fun onClick(view: View, position: Int) {
                showDetail(solutions!![position].solIdx, solutions!![position].descHeader)
            }
        }
    }

    private fun showDetail(solutionIdx: String, title: String) = with(binding.layoutDetail) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = solutionService.getSolDetail(solutionIdx, tokenManager.getAccessToken()!!)

                withContext(Dispatchers.Main) {
                    if (response.isSuccess) {
                        val data = response.data!!

                        Log.i(TAG, "detailIdx: ${data.detailIdx}")
                        Log.i(TAG, "detailHeader: ${data.detailHeader}")
                        Log.i(TAG, "imageUrls: ${data.imageUrls}")
                        Log.i(TAG, "content: ${data.content}")
                        Log.i(TAG, "subContent: ${data.subContent}")

                        // Detail Title
                        textviewTitle.text = title
                        // Detail Content Title
                        textviewLevelATitle.text = data.detailHeader
                        // Detail Content Text
                        val contentList: List<String> = data.content.split("\\n")
                        val adapter = DetailAdapter(contentList)
                        recyclerviewContent.adapter = adapter
                        recyclerviewContent.layoutManager = LinearLayoutManager(this@SolutionBeforeActivity, LinearLayoutManager.VERTICAL, false)

                        if (data.imageUrls.isEmpty()){
                            Log.i(TAG, "이미지 로딩 실패")
                        } else {
                            Glide.with(this@SolutionBeforeActivity)
                                .load(data.imageUrls[0])
                                .into(imageContent)
                        }

                        if (!isLayoutVisible) {
                            root.visibility = View.VISIBLE
                            root.translationY = root.height.toFloat()
                            root.translationZ = Float.MAX_VALUE // 가장 위로 나오게
                            root.animate().translationY(0f).setDuration(300).start()
                            isLayoutVisible = true
                        }

                    } else {
                        Log.e(TAG, "솔루션 디테일 취득 실패")
                        Log.e(TAG, "${response.code} ${response.message}")
                        showToast("솔루션 디테일 취득 실패")
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "서버 통신 오류: ${e.message}")
                    showToast("솔루션 디테일 취득 실패")
                }
            }
        }
    }

    private fun getSolution(surveyIdx: Int, pattern: Int, level: Int) = with(binding) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val request = SolutionEntryReq(surveyIdx, pattern, level)
                val response = solutionService.getSolEntries(tokenManager.getAccessToken()!!, request)

                withContext(Dispatchers.Main) {
                    if (response.isSuccess) {
                        val data = response.data!!

                        Log.i(TAG, "entryIdx: ${data.entryIdx}")
                        Log.i(TAG, "level: ${data.level}")
                        Log.i(TAG, "problem name: ${data.problemName}")
                        Log.i(TAG, "solution list: ${data.solList}")
                        Log.i(TAG, "possible solution list: ${data.solPList}")
                        Log.i(TAG, "solution bypass list: ${data.solBList}")

                        Log.i(TAG, "data: ${data}")

                        solutions = data.solList
                        for (i in data.solPList.indices) {
                            solutionPs?.add(data.solPList[i].possibleName)
                        }
                        if (data.solBList != null) {
                            solutionBs = data.solBList.toMutableList()
                        }

                        // 타이틀 설정
                        title.text = data.problemName

                        // 발생 가능한 문제 설정
                        textAvailProblems.text = solutionPs?.joinToString("\n")

                        // TODO("solutions에 따라 동적 버튼 생성")
                        solutionBtn()

                    } else {
                        Log.e(TAG, "솔루션 검색 실패")
                        Log.e(TAG, "${response.code} ${response.message}")
                        showToast("솔루션 검색 실패")
                    }
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e(TAG, "서버 통신 오류: ${e.message}")
                    showToast("솔루션 검색 실패")
                }
            }
        }
    }

    private fun setTouchListener() = with(binding) {
        layoutRoot.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                closeLayout()
                return@setOnTouchListener true
            }
            false
        }

        fieldScroll.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                closeLayout()
                return@setOnTouchListener true
            }
            false
        }

        layoutDetail.root.setOnTouchListener { _, _ -> true }
    }
    // detail layout 비표시
    private fun closeLayout() = with(binding.layoutDetail) {
        if (isLayoutVisible) {
            root.animate().translationY(root.height.toFloat()).setDuration(300).withEndAction {
                root.visibility = View.GONE
            }.start()
            isLayoutVisible = false
        }
    }


    companion object {
        const val TAG = "Before Solution"
    }
}