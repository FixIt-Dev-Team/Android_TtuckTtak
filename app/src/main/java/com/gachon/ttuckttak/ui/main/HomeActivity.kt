package com.gachon.ttuckttak.ui.main

import android.util.Log
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.data.local.database.AppDatabase
import com.gachon.ttuckttak.databinding.ActivityHomeBinding
import com.gachon.ttuckttak.ui.problem.ProblemCategoryActivity
import com.gachon.ttuckttak.ui.setting.SettingsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : BaseActivity<ActivityHomeBinding>(ActivityHomeBinding::inflate) {
    private val database by lazy { AppDatabase.getInstance(this@HomeActivity)!! }

    override fun initAfterBinding() {
        setUi()
        setClickListener()
    }

    private fun setUi() = with(binding) {
        CoroutineScope(Dispatchers.Default).launch {
            val diagnosisDao = database.diagnosisDao()

            // Test
//            val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm")
//            diagnosisDao.insertDiagnosis(Diagnosis("부팅디스크(SSD) 접촉불량 예상", dateFormat.format(Date(System.currentTimeMillis()))))

            val diagnosis = diagnosisDao.getDiagnosis()
            Log.d("diagnosis", diagnosis.toString())

            runOnUiThread {
                if (diagnosis != null) {
                    textviewLatestResultText.text = diagnosis.context
                    textviewLatestResultTime.text = diagnosis.date
                }
            }
        }
    }

    private fun setClickListener() = with(binding) {
        imagebuttonStart.setOnClickListener {
            startNextActivity(ProblemCategoryActivity::class.java)
        }

        buttonSetting.setOnClickListener {
            startNextActivity(SettingsActivity::class.java)
        }
    }
}