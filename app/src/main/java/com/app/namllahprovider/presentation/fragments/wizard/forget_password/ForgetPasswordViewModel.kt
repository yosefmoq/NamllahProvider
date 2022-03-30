package com.app.namllahprovider.presentation.fragments.wizard.forget_password

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.app.namllahprovider.data.api.auth.forget_password.ForgetPasswordResponse
import com.app.namllahprovider.domain.repository.AuthRepository
import com.app.namllahprovider.domain.repository.ConfigRepository
import com.app.namllahprovider.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForgetPasswordViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
) : BaseViewModel(application) {

    var sendOTPCodeLiveData = MutableLiveData<ForgetPasswordResponse?>()

    fun sendOTPCode(phoneNumber: String) = launch {
        changeLoadingStatus(true)
        disposable.add(
            authRepository
                .forgetPassword(phoneNumber = phoneNumber)
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    sendOTPCodeLiveData.postValue(it)
                    changeLoadingStatus(false)
                }, {
                    sendOTPCodeLiveData.postValue(null)
                    changeErrorMessage(it)
                    changeLoadingStatus(false)
                }, {
                    sendOTPCodeLiveData.postValue(null)
                    changeLoadingStatus(false)
                })
        )
    }
}