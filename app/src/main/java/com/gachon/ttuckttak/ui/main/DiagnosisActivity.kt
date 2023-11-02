package com.gachon.ttuckttak.ui.main

import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.gachon.ttuckttak.base.BaseActivity
import com.gachon.ttuckttak.databinding.ActivityDiagnosisBinding
import com.gachon.ttuckttak.ui.main.DiagnosisViewModel.NavigateTo.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DiagnosisActivity : BaseActivity<ActivityDiagnosisBinding>(
    ActivityDiagnosisBinding::inflate,
    TransitionMode.HORIZONTAL
) {

    private val viewModel: DiagnosisViewModel by viewModels()
    private lateinit var adapter: DiagnosisAdapter

    override fun initAfterBinding() {
        binding.viewmodel = viewModel
        binding.lifecycleOwner = this@DiagnosisActivity

        adapter = DiagnosisAdapter()
        binding.recyclerviewDiagnosis.layoutManager = LinearLayoutManager(this@DiagnosisActivity)
        binding.recyclerviewDiagnosis.adapter = adapter

        setObservers()
    }

    private fun setObservers() {
        viewModel.diagnosisList.observe(this@DiagnosisActivity) { diagnosisList ->
            diagnosisList?.let { data ->
                adapter.submitList(data)
            }
        }

        viewModel.viewEvent.observe(this@DiagnosisActivity) { event ->
            event.getContentIfNotHandled()?.let { navigateTo ->
                when (navigateTo) {
                    is Back -> finish()
                }
            }
        }
    }
}