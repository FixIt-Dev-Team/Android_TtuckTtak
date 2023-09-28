package com.gachon.ttuckttak.ui.terms

import com.gachon.ttuckttak.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TermsUseViewmodel @Inject constructor() : BaseViewModel() {

    fun goBack() = viewEvent(NavigateTo.Back)

    sealed class NavigateTo {
        object Back : NavigateTo()
    }
}