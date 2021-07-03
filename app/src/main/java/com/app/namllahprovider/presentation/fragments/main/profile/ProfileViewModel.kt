package com.app.namllahprovider.presentation.fragments.main.profile

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.app.namllahprovider.data.api.BaseResponse
import com.app.namllahprovider.data.api.user.update_user_profile.UpdateUserProfileRequest
import com.app.namllahprovider.data.api.user.update_user_profile.UpdateUserProfileResponse
import com.app.namllahprovider.data.model.AreaDto
import com.app.namllahprovider.data.model.ServiceDto
import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.domain.repository.ConfigRepository
import com.app.namllahprovider.domain.repository.GlobalRepository
import com.app.namllahprovider.domain.repository.UserRepository
import com.app.namllahprovider.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Maybe
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    application: Application,
    private val configRepository: ConfigRepository,
    private val userRepository: UserRepository,
    private val globalRepository: GlobalRepository
) : BaseViewModel(application) {

    //    var loggedUserLiveData = MediatorLiveData<UserDto?>()
    val getLoggedUserLiveData = MutableLiveData<UserDto?>()
    val updateProfileLiveData = MutableLiveData<UpdateUserProfileResponse?>()
    val getServicesLiveData = MutableLiveData<List<ServiceDto>>()
    val getAreasLiveData = MutableLiveData<List<AreaDto>>()
    val logoutLiveData = MutableLiveData<BaseResponse?>()

    fun getLoggedUser() = launch {
        getLoggedUserLiveData.postValue(configRepository.getLoggedUser())
    }

    fun getLoggedUserApi() = launch {
        changeLoadingStatus(true, "getLoggedUser")
        disposable.add(
            userRepository
                .getUserProfile()
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    Timber.tag(TAG).d("getLoggedUser : $it")
                    getLoggedUserLiveData.postValue(it)
                    configRepository.setLoggedUser(it)
                    configRepository.setLanguage(it.language.code)
                    changeLoadingStatus(false, "getLoggedUserApi")
                }, {
                    getLoggedUserLiveData.postValue(null)
                    changeErrorMessage(it)
                    changeLoadingStatus(false, "getLoggedUserApi")
                }, {
                    getLoggedUserLiveData.postValue(null)
                    changeLoadingStatus(false, "getLoggedUserApi")
                })
        )
    }

    fun updateUsername(newName: String) = launch {
        changeLoadingStatus(true, "")
        disposable.add(
            userRepository
                .updateUserProfile(UpdateUserProfileRequest(name = newName))
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    updateProfileLiveData.postValue(it)
                    changeLoadingStatus(false, "updateUsername")
                }, {
                    updateProfileLiveData.postValue(null)
                    changeErrorMessage(it)
                    changeLoadingStatus(false, "updateUsername")
                }, {
                    updateProfileLiveData.postValue(null)
                    changeLoadingStatus(false, "updateUsername")
                })
        )
    }

    fun updatePhoneNumber(newPhone: String) = launch {
        changeLoadingStatus(true)
        disposable.add(
            userRepository
                .updateUserProfile(UpdateUserProfileRequest(mobile = newPhone))
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    updateProfileLiveData.postValue(it)
                    changeLoadingStatus(false, "updatePhoneNumber")
                }, {
                    updateProfileLiveData.postValue(null)
                    changeErrorMessage(it)
                    changeLoadingStatus(false, "updatePhoneNumber")
                }, {
                    updateProfileLiveData.postValue(null)
                    changeLoadingStatus(false, "updatePhoneNumber")
                })
        )
    }

    fun updateLanguage(newLanguage: String) = launch {
        changeLoadingStatus(true)
        disposable.add(
            userRepository
                .updateUserProfile(UpdateUserProfileRequest(language = newLanguage))
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    updateProfileLiveData.postValue(it)
                    changeLoadingStatus(false, "updateLanguage")
                }, {
                    updateProfileLiveData.postValue(null)
                    changeErrorMessage(it)
                    changeLoadingStatus(false, "updateLanguage")
                }, {
                    updateProfileLiveData.postValue(null)
                    changeLoadingStatus(false, "updateLanguage")
                })
        )
    }

    fun updateSettings(key: String, value: Boolean) = launch {
        Timber.tag(TAG).d("updateSettings : key $key, value $value")
//        changeLoadingStatus(true)
        disposable.add(
            userRepository
                .updateUserSettings(key, value)
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe()
        )
    }

    fun updatePassword(oldPassword: String, newPassword: String) = launch {
//        changeLoadingStatus(true,"updatePassword")
        disposable.add(
            userRepository
                .updateUserProfile(
                    UpdateUserProfileRequest(
                        oldPassword = oldPassword,
                        newPassword = newPassword
                    )
                )
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    Timber.tag(TAG).d("updatePassword : $it")
                    updateProfileLiveData.postValue(it)
                    changeLoadingStatus(false, "updatePassword")
                }, {
                    updateProfileLiveData.postValue(null)
                    changeErrorMessage(it)
                    changeLoadingStatus(false, "updatePassword")
                }, {
                    updateProfileLiveData.postValue(null)
                    changeLoadingStatus(false, "updatePassword")
                })
        )
    }

    fun updateServices(newServiceId: Int) = launch {
        changeLoadingStatus(true, "updateServices")
        val currentUser = configRepository.getLoggedUser()!!
        val currentServicesIds =
            currentUser.services.servicesList.map { it.id }.toMutableList()
        /*       if (currentServicesIds.contains(newServiceId)) {
                   //User have this service already
                   changeLoadingStatus(false, "updateServices")
               } else {*/
        currentServicesIds.add(newServiceId)
        disposable.add(
            userRepository
                .updateUserProfile(UpdateUserProfileRequest(services_id = currentServicesIds.toIntArray()))
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    updateProfileLiveData.postValue(it)
                    changeLoadingStatus(false, "updateServices")
                }, {
                    updateProfileLiveData.postValue(null)
                    changeErrorMessage(it)
                    changeLoadingStatus(false, "updateServices")
                }, {
                    updateProfileLiveData.postValue(null)
                    changeLoadingStatus(false, "updateServices")
                })
        )
//        }

    }

    fun getServicesAndAreas() = launch {
        changeLoadingStatus(true)
        var serviceList = listOf<ServiceDto>()
        var areasList = listOf<AreaDto>()
        disposable.add(
            Maybe.just(true)
                .flatMap {
                    globalRepository.getServices()
                        .switchIfEmpty(Maybe.create { it.onSuccess(listOf()) })
                }.flatMap {
                    serviceList = it
                    globalRepository.getAreas()
                        .switchIfEmpty(Maybe.create { it.onSuccess(listOf()) })
                }
                .map {
                    areasList = it
                    true
                }
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    Timber.tag(TAG).d("getServicesAndAreas : SUCCESS")
                    if (it) {
                        getServicesLiveData.postValue(serviceList)
                        getAreasLiveData.postValue(areasList)
                    }
                    changeLoadingStatus(false, "getServicesAndAreas")
                }, {
                    Timber.tag(TAG).d("getServicesAndAreas : ERROR")
                    getServicesLiveData.postValue(serviceList)
                    getAreasLiveData.postValue(areasList)
                    changeErrorMessage(it)
                    changeLoadingStatus(false, "getServicesAndAreas")
                }, {
                    Timber.tag(TAG).d("getServicesAndAreas : COMPLETE")
                    getServicesLiveData.postValue(serviceList)
                    getAreasLiveData.postValue(areasList)
                    changeLoadingStatus(false, "getServicesAndAreas")
                })
        )
    }

    fun logout() {
        launch {
            changeLoadingStatus(true, "Logging out ...")
            disposable.add(
                userRepository
                    .logout()
                    .subscribeOn(ioScheduler)
                    .observeOn(ioScheduler)
                    .subscribe({
                        logoutLiveData.postValue(it)
                        changeLoadingStatus(false)
                    }, {
                        logoutLiveData.postValue(null)
                        changeErrorMessage(it)
                        changeLoadingStatus(false)
                    }, {
                        logoutLiveData.postValue(null)
                        notifyNoDataComing()
                        changeLoadingStatus(false)
                    })
            )
        }
    }

    fun clearConfigData() = launch {
        configRepository.clearConfigData()
        configRepository.setSeenOnBoarding(true)
    }

    fun updateUserImage(body: MultipartBody.Part) {
        changeLoadingStatus(true)
        disposable.add(
            userRepository
                .updateUserProfile(UpdateUserProfileRequest(image = body))
                .subscribeOn(ioScheduler)
                .observeOn(ioScheduler)
                .subscribe({
                    updateProfileLiveData.postValue(it)
                    changeLoadingStatus(false, "updateUserImage")
                }, {
                    updateProfileLiveData.postValue(null)
                    changeErrorMessage(it)
                    changeLoadingStatus(false, "updateUserImage")
                }, {
                    updateProfileLiveData.postValue(null)
                    changeLoadingStatus(false, "updateUserImage")
                })
        )
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}

