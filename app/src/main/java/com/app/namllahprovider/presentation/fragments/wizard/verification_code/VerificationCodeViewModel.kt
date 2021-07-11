package com.app.namllahprovider.presentation.fragments.wizard.verification_code

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.app.namllahprovider.data.api.auth.check_reset_password.CheckResetPasswordResponse
import com.app.namllahprovider.data.api.auth.verification_code.VerificationCodeResponse
import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.domain.repository.AuthRepository
import com.app.namllahprovider.domain.repository.ConfigRepository
import com.app.namllahprovider.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerificationCodeViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val configRepository: ConfigRepository,
    private val authRepository: AuthRepository,
) : BaseViewModel(application) {

    var verificationCodeLiveData = MutableLiveData<VerificationCodeResponse?>()
    var checkResetPasswordLiveData = MutableLiveData<CheckResetPasswordResponse?>()

    fun verifyOTPCode(phoneNumber: String, code: String) =
        launch {
            changeLoadingStatus(true)
            disposable.add(
                authRepository
                    .verifyOTPCode(phoneNumber, code)
                    .subscribeOn(ioScheduler)
                    .observeOn(ioScheduler)
                    .subscribe({
                        verificationCodeLiveData.postValue(it)
                        changeLoadingStatus(false)
                    }, {
                        changeErrorMessage(it)
                        verificationCodeLiveData.postValue(null)
                        changeLoadingStatus(false)
                    }, {
                        verificationCodeLiveData.postValue(null)
                        changeLoadingStatus(false)
                    })
            )
        }

    fun checkResetCode(phoneNumber: String, code: String) =
        launch {
            changeLoadingStatus(true)
            disposable.add(
                authRepository
                    .checkResetPassword(phoneNumber, code)
                    .subscribeOn(ioScheduler)
                    .observeOn(ioScheduler)
                    .subscribe({
                        checkResetPasswordLiveData.postValue(it)
                        changeLoadingStatus(false)
                    }, {
                        changeErrorMessage(it)
                        checkResetPasswordLiveData.postValue(null)
                        changeLoadingStatus(false)
                    }, {
                        checkResetPasswordLiveData.postValue(null)
                        changeLoadingStatus(false)
                    })
            )
        }

    fun saveUserDataLocal(userDto: UserDto) = launch {
        configRepository.setLoggedUser(userDto)
    }

    fun saveUserTokenLocal(userToken: String) = launch {
        configRepository.setUserToken(userToken)
    }

    fun changeLoginStatus(newLoginStatus: Boolean) = launch {
        configRepository.setLogin(newLoginStatus)
    }
}