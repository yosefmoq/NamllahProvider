package com.app.namllahprovider.presentation

import android.app.Application
import com.app.namllahprovider.domain.repository.ConfigRepository
import com.app.namllahprovider.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WizardViewModel @Inject constructor(
    application: Application,
    private val configRepository: ConfigRepository
) : BaseViewModel(application) {

    fun getLoggedUser() = configRepository.getLoggedUser()

}