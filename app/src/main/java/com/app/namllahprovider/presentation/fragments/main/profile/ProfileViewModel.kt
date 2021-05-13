package com.app.namllahprovider.presentation.fragments.main.profile

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.domain.repository.ConfigRepository
import com.app.namllahprovider.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val configRepository: ConfigRepository
) : BaseViewModel(application) {

    var loggedUserLiveData = MediatorLiveData<UserDto?>()

    fun getLoggedUser() = launch {
        //Should get profile from API and update SP
        loggedUserLiveData.postValue(configRepository.getLoggedUser())
    }
}