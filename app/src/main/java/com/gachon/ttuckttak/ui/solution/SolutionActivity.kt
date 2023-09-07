package com.gachon.ttuckttak.ui.solution

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.gachon.ttuckttak.R
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.TokenManager
import com.gachon.ttuckttak.data.local.dao.DiagnosisDao
import com.gachon.ttuckttak.data.local.entity.Diagnosis
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionBypassDto
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionDto
import com.gachon.ttuckttak.data.remote.dto.solution.SolutionEntryReq
import com.gachon.ttuckttak.data.remote.service.SolutionService
import com.gachon.ttuckttak.databinding.ActivitySolutionBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class SolutionActivity : BaseActivity<ActivitySolutionBinding>(ActivitySolutionBinding::inflate, TransitionMode.HORIZONTAL) {

    private val tokenManager: TokenManager by lazy { TokenManager(this@SolutionActivity) }
    @Inject lateinit var diagnosisDao: DiagnosisDao

    private var solutions: List<SolutionDto>? = null
    private var solutionPs: MutableList<String>? = mutableListOf()
    private var solutionBs: List<SolutionBypassDto>? = null
    private var isLayoutVisible = false

    @Inject lateinit var solutionService: SolutionService

    override fun initAfterBinding() {
        if (intent.hasExtra("surveyIdx")){
            val surveyIdx = intent.getIntExtra("surveyIdx", 0)
            val pattern = intent.getIntExtra("pattern", -1)
            val level = intent.getIntExtra("level", 0)

            Log.i(TAG, "surveyIdx: $surveyIdx")
            Log.i(TAG, "resPattern: $pattern")
            Log.i(TAG, "level: $level")

            getSolutionSurvey(surveyIdx, pattern, level)
        } else if (intent.hasExtra("entryID")){
            val entryID = intent.getIntExtra("entryID", 0)

            Log.i(TAG, "Bypass entryID: $entryID")

            getSolutionEntry(entryID)
        }

        setClickListener()
        setTouchListner()
    }

    private fun setClickListener() = with(binding) {
        buttonBack.setOnClickListener {
            finish()
        }

        buttonCs.setOnClickListener {
            shadow.visibility = View.VISIBLE
            showLayout()
        }

        layoutCs.buttonConfirm.setOnClickListener {
            val clipboard: ClipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("label", "ttukttak@ttukttak.com")
            clipboard.setPrimaryClip(clipData)

            shadow.visibility = View.INVISIBLE
            closeLayout()
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
                if (isLayoutVisible) {
                    return
                }

                val solution = solutionList[position]

                CoroutineScope(Dispatchers.Default).launch {
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm", java.util.Locale.getDefault())
                    diagnosisDao.insertDiagnosis(Diagnosis(tokenManager.getAccessToken()!! ,solution.descHeader, dateFormat.format(Date(System.currentTimeMillis()))))
                }
                val intent = Intent(this@SolutionActivity, SolutionDescActivity::class.java)
                intent.putExtra("solIdx", solution.solIdx)
                intent.putExtra("progress", 0)
                intent.putExtra("solutionBs", solutionBs?.toTypedArray())

                startActivity(intent)
            }
        })
    }

    private fun getSolutionSurvey(surveyIdx: Int, pattern: Int, level: Int) = with(binding) {
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
                            solutionBs = data.solBList
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

    private fun getSolutionEntry(entryID: Int) = with(binding) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val response = solutionService.getSolbyEntry(entryID, tokenManager.getAccessToken()!!)

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
                            solutionBs = data.solBList
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

    companion object {
        const val TAG = "Solution"
    }
}