package com.app.namllahprovider.presentation.fragments.main.profile

import android.app.Application
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.domain.repository.ConfigRepository
import com.app.namllahprovider.domain.repository.UserRepository
import com.app.namllahprovider.presentation.base.BaseViewModel
import com.app.namllahprovider.presentation.fragments.main.home.HomeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val configRepository: ConfigRepository,
    private val userRepository: UserRepository
) : BaseViewModel(application) {

//    var loggedUserLiveData = MediatorLiveData<UserDto?>()
    val getLoggedUserLiveData = MutableLiveData<UserDto?>()

    fun getLoggedUser() = launch {
        changeLoadingStatus(true)
        disposable.add(
            userRepository
                .getUserProfile()
                .subscribeOn(ioScheduler)
                .observeOn(newThreadScheduler)
                .subscribe({
                    launch {
                        delay(1000)
                        getLoggedUserLiveData.postValue(it)
                        changeLoadingStatus(false)
                    }
                }, {
                    getLoggedUserLiveData.postValue(null)
                    changeErrorMessage(it)
                    changeLoadingStatus(false)

                }, {
                    getLoggedUserLiveData.postValue(null)
                    changeLoadingStatus(false)
                })
        )
    }
}