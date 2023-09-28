package com.gachon.ttuckttak.ui.main

import androidx.lifecycle.LiveData
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.data.local.entity.Diagnosis
import com.gachon.ttuckttak.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val userRepository: UserRepository) : BaseViewModel() {

    val diagnosis: LiveData<Diagnosis> = userRepository.getRecentDiagnosis()

    fun startProblemCategoryActivity() = viewEvent(NavigateTo.ProblemCategory)

    fun showDiagnosis() = viewEvent(NavigateTo.Diagnosis)

    fun startSettingsActivity() = viewEvent(NavigateTo.Settings)

    sealed class NavigateTo {
        object ProblemCategory : NavigateTo()
        object Diagnosis : NavigateTo()
        object Settings : NavigateTo()
    }

}