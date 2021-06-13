package com.app.namllahprovider.presentation.fragments.main.notifiactions

import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.app.namllahprovider.data.model.NotificationDto
import com.app.namllahprovider.domain.repository.NotificationRepository
import com.app.namllahprovider.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val notificationRepository: NotificationRepository
) : BaseViewModel(application) {

    val getListNotificationLiveData = MutableLiveData<List<NotificationDto>?>()

    fun getListNotificationRequest(pageNumber :Int) {
        launch {
            changeLoadingStatus(true)
            disposable.add(
                notificationRepository.getAllNotification(pageNumber)
                    .subscribeOn(ioScheduler)
                    .observeOn(ioScheduler)
                    .subscribe({
                        getListNotificationLiveData.postValue(it)
                        changeLoadingStatus(false)
                    }, {
                        getListNotificationLiveData.postValue(null)
                        changeLoadingStatus(false)
                        changeErrorMessage(it)
                    }, {
                        getListNotificationLiveData.postValue(null)
                        changeLoadingStatus(false)
                    })
            )
        }
    }

}