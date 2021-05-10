package com.app.namllahprovider.presentation.fragments.wizard.on_boarding

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import com.app.namllahprovider.domain.repository.ConfigRepository
import com.app.namllahprovider.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnBoardingViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val configRepository: ConfigRepository
) : BaseViewModel(application) {

    fun changeOnBoardingStatus(status: Boolean) {
        configRepository.setSeenOnBoarding(status)
    }
}