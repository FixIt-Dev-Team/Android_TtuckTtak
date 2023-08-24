package com.gachon.ttuckttak.ui.solution

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionBypassDto
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionDto
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionEntryReq
import com.gachon.ttuckttak.data.remote.service.SolutionService
import com.gachon.ttuckttak.databinding.ActivitySolutionBinding
import com.gachon.ttuckttak.utils.SolutionAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import javax.inject.Inject

@AndroidEntryPoint
class SolutionActivity : BaseActivity<ActivitySolutionBinding>(ActivitySolutionBinding::inflate, TransitionMode.HORIZONTAL) {

    private val tokenManager: TokenManager by lazy { TokenManager(this@SolutionActivity) }

    private var solutions: List<SolutionDto>? = null
    private var solutionPs: MutableList<String>? = mutableListOf()
    private var solutionBs: MutableList<SolutionBypassDto>? = null

    @Inject lateinit var solutionService: SolutionService

    override fun initAfterBinding() {
        val surveyIdx = intent.getIntExtra("surveyIdx", 0)
        val pattern = intent.getIntExtra("pattern", -1)
        val level = intent.getIntExtra("level", 0)

        Log.i(TAG, "surveyIdx: $surveyIdx")
        Log.i(TAG, "resPattern: $pattern")
        Log.i(TAG, "level: $level")

        getSolution(surveyIdx, pattern, level)
        setClickListener()
    }

    private fun setClickListener() = with(binding) {
        buttonBack.setOnClickListener {
            finish()
        }

        fieldButtonMenu.setOnClickListener {
            finish()
        }

        buttonCs.setOnClickListener {
            TODO("고객센터 연결")
        }

        buttonComplete.setOnClickListener {
            finish()
        }
    }

    private fun solutionBtn(solutionList: List<SolutionDto>) = with(binding) {
        val adapter = SolutionAdapter(solutionList)
        buttonListView.adapter = adapter
        buttonListView.layoutManager = LinearLayoutManager(this@SolutionActivity, LinearLayoutManager.VERTICAL, false)

        adapter.setItemClickListener(object: SolutionAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                val intent = Intent(this@SolutionActivity, SolutionDescActivity::class.java)
                intent.putExtra("solIdx", solutionList[position].solIdx)
                intent.putExtra("progress", 0)
                intent.putExtra("solTitle", solutionList[position].descHeader)
                startActivity(intent)
            }
        })
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

                        solutions = data.solList
                        for (i in data.solPList.indices) {
                            solutionPs?.add(data.solPList[i].possibleName)
                        }
                        if (data.solBList != null) {
                            solutionBs = data.solBList.toMutableList()
                        }

                        // 타이틀 설정
                        title.text = data.problemName

                        // 카운터 설정
                        textCount.text = getString(R.string.solutions, solutions!!.size)

                        // 발생 가능한 문제 설정
                        textAvailProblems.text = solutionPs?.joinToString("\n")

                        // RecyclerView 설정
                        solutionBtn(solutions!!)

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

    companion object {
        const val TAG = "Solution"
    }
}