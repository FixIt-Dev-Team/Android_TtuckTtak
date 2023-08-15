package com.gachon.ttuckttak.ui.solution

import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.remote.TtukttakServer
import com.gachon.ttuckttak.data.remote.dto.SolutionBypassDto
import com.gachon.ttuckttak.data.remote.dto.SolutionDto
import com.gachon.ttuckttak.data.remote.dto.SolutionList
import com.gachon.ttuckttak.data.remote.dto.SolutionPossibleDto
import com.gachon.ttuckttak.databinding.ActivitySolutionBeforeBinding
import com.gachon.ttuckttak.ui.setting.SettingsProfileActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception

class SolutionBeforeActivity : BaseActivity<ActivitySolutionBeforeBinding>(ActivitySolutionBeforeBinding::inflate) {

    private val tokenManager: TokenManager by lazy { TokenManager(this@SolutionBeforeActivity) }

    private var solutions: SolutionList? = null
    private var solutionPs: MutableList<String>? = mutableListOf()
    private var solutionBs: MutableList<SolutionBypassDto>? = null

    override fun initAfterBinding() = with(binding) {
        val entryIdx = intent.getIntExtra("surveyIdx", 0)
        val resPattern = intent.getStringExtra("pattern")!!.toInt(2)

        // 솔루션 정보 API 수신 및 뷰 텍스트에 할당
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = TtukttakServer.getSolbyEntry(entryIdx, tokenManager.getAccessToken()!!)

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
                        // TODO("subtitle 텍스트 내용 설계되어 있지 않음")

                        // 발생 가능한 문제 설정
                        textAvailProblems.text = solutionPs?.joinToString("\n")

                        // TODO("solutions에 따라 동적 버튼 생성")

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

        setClickListener()
    }

    private fun setClickListener() = with(binding) {
        fieldButtonBack.setOnClickListener {
            finish()
        }

        buttonFindSolution.setOnClickListener {
            TODO("Go to Solution Page")
            startNextActivity(SolutionActivity::class.java)
        }
    }

    companion object {
        const val TAG = "Before Solution"
    }
}