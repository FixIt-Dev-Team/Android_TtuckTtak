package com.gachon.ttuckttak.ui.main

import androidx.lifecycle.LiveData
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.data.local.entity.Diagnosis
import com.gachon.ttuckttak.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DiagnosisViewModel @Inject constructor(
    private val userRepository: UserRepository
) : BaseViewModel() {

    val diagnosisList: LiveData<List<Diagnosis>> = userRepository.getLatestDiagnosis()

    fun goBack() = viewEvent(NavigateTo.Back)

    sealed class NavigateTo {
        object Back : NavigateTo()
    }

}