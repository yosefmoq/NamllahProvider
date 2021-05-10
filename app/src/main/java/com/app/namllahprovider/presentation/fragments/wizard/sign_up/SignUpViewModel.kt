package com.app.namllahprovider.presentation.fragments.wizard.sign_up

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.app.namllahprovider.data.api.auth.sign_up.SignUpResponse
import com.app.namllahprovider.domain.repository.AuthRepository
import com.app.namllahprovider.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val authRepository: AuthRepository,
) : BaseViewModel(application) {
    var signUpLiveData = MutableLiveData<SignUpResponse?>()

    fun signUpRequest(userName: String, phoneNumber: String, password: String, language: String) =
        launch {
            changeLoadingStatus(true)
            disposable.add(
                authRepository
                    .signUp(userName, phoneNumber, password, language)
                    .subscribeOn(ioScheduler)
                    .observeOn(ioScheduler)
                    .subscribe({
                        signUpLiveData.postValue(it)
                        changeLoadingStatus(false)
                    }, {
                        signUpLiveData.postValue(null)
                        changeLoadingStatus(false)
                        changeErrorMessage(it)
                    }, {
                        signUpLiveData.postValue(null)
                        changeLoadingStatus(false)
                    })
            )
        }


}