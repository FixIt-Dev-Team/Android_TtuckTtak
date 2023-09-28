package com.gachon.ttuckttak.ui.login

import androidx.lifecycle.viewModelScope
import com.gachon.ttuckttak.base.BaseViewModel
import com.gachon.ttuckttak.repository.user.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPwViewmodel @Inject constructor(
    private val userRepository: UserRepository,
) : BaseViewModel() {

    lateinit var email: String

    init {
        viewModelScope.launch(Dispatchers.IO) {
            email = userRepository.getPasswordResetEmail()!!
        }
    }

    fun goLandingActivity() = viewEvent(NavigateTo.Landing)

    sealed class NavigateTo {
        object Landing : NavigateTo()
    }

}