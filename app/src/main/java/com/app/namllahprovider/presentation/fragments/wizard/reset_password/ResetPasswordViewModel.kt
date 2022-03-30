package com.app.namllahprovider.presentation.fragments.wizard.reset_password

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.app.namllahprovider.data.api.auth.reset_password.ResetPasswordResponse
import com.app.namllahprovider.domain.repository.AuthRepository
import com.app.namllahprovider.domain.repository.ConfigRepository
import com.app.namllahprovider.presentation.base.BaseViewModel
import com.app.namllahprovider.presentation.fragments.wizard.sign_in.SignInViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val configRepository: ConfigRepository,
    private val authRepository: AuthRepository,
) : BaseViewModel(application) {

    var resetPasswordLiveData = MutableLiveData<ResetPasswordResponse?>()

    fun resetPassword(phoneNumber: String, newPassword: String, code: String) = launch {
        changeLoadingStatus(true)
        disposable.add(
            authRepository
                .resetPassword(phoneNumber = phoneNumber , password = newPassword , code = code)
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    resetPasswordLiveData.postValue(it)
                    changeLoadingStatus(false)
                },{
                    resetPasswordLiveData.postValue(null)
                    changeErrorMessage(it)
                    changeLoadingStatus(false)
                },{
                    resetPasswordLiveData.postValue(null)
                    changeLoadingStatus(false)
                })
        )
    }
    fun saveUserTokenLocal(userToken: String) = launch {
        configRepository.setUserToken(userToken)
    }

    fun changeLoginStatus(newLoginStatus: Boolean) = launch {
        configRepository.setLogin(newLoginStatus)
    }
}