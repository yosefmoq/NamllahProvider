package com.app.namllahprovider.presentation.fragments.wizard.sign_in

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.app.namllahprovider.data.api.auth.sign_in.SignInResponse
import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.domain.repository.AuthRepository
import com.app.namllahprovider.domain.repository.ConfigRepository
import com.app.namllahprovider.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val configRepository: ConfigRepository,
    private val authRepository: AuthRepository,
) : BaseViewModel(application) {

    var signInLiveData = MutableLiveData<SignInResponse?>()

    fun signInRequest(phoneNumber: String, password: String) {
        launch {
            changeLoadingStatus(true)
            disposable.add(
                authRepository.signIn(phoneNumber, password)
                    .subscribeOn(ioScheduler)
                    .observeOn(ioScheduler)
                    .subscribe({
                        signInLiveData.postValue(it)
                        changeLoadingStatus(false)
                    }, {
                        println("ERROR :: $it")
                        signInLiveData.postValue(null)
                        changeLoadingStatus(false)
                        changeErrorMessage(it)
                    }, {
                        signInLiveData.postValue(null)
                        changeLoadingStatus(false)
                    })
            )
        }
    }

//    fun saveUserDataLocal(userDto: UserDto) = launch {
//        configRepository.setLoggedUser(userDto)
//    }

    fun saveUserTokenLocal(userToken: String) = launch {
        Timber.tag(TAG).d("saveUserTokenLocal : userToken $userToken")
        configRepository.setUserToken(userToken)
    }

    fun changeLoginStatus(newLoginStatus: Boolean) = launch {
        configRepository.setLogin(newLoginStatus)
    }

    companion object{
        private const val TAG = "SignInViewModel"
    }
}