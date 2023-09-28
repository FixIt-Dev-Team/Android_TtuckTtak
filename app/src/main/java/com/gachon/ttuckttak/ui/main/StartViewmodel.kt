package com.gachon.ttuckttak.ui.main

import com.gachon.ttuckttak.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartViewmodel @Inject constructor() : BaseViewModel() {

    fun goNext() = viewEvent(NavigateTo.Home)

    sealed class NavigateTo {
        object Home : NavigateTo()
    }
}