package com.gachon.ttuckttak.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.data.local.entity.Diagnosis
import com.gachon.ttuckttak.repository.DiagnosisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val diagnosisRepository: DiagnosisRepository) : BaseViewModel() {

    private val _diagnosis = MutableLiveData<Diagnosis?>()
    val diagnosis: LiveData<Diagnosis?>
        get() = _diagnosis

    init {
        getDiagnosis()
    }

    private fun getDiagnosis() = viewModelScope.launch {
        withContext(Dispatchers.Default) {
            _diagnosis.postValue(diagnosisRepository.getDiagnosis())
            Log.i("Diagnosis", diagnosis.value.toString())
        }
    }

    fun startProblemCategoryActivity() = viewEvent(NavigateTo.ProblemCategory)

    fun startSettingsActivity() = viewEvent(NavigateTo.Settings)

    sealed class NavigateTo {
        object ProblemCategory : NavigateTo()
        object Settings : NavigateTo()
    }

}