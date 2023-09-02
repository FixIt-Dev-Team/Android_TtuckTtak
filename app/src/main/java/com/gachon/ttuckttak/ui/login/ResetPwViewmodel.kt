package com.gachon.ttuckttak.ui.login

import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.data.local.UserManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResetPwViewmodel @Inject constructor(
    private val userManager: UserManager,
) : BaseViewModel() {

    val email: String = userManager.getPasswordResetEmail()!!

    fun goLandingActivity() = viewEvent(NavigateTo.Landing)

    sealed class NavigateTo {
        object Landing : NavigateTo()
    }

}