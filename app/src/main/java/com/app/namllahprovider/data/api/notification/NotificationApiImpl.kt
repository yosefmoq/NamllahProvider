package com.app.namllahprovider.data.api.notification

import com.app.namllahprovider.data.api.BaseResponse
import com.app.namllahprovider.data.api.notification.update_fcm.UpdateFCMTokenRequest
import com.app.namllahprovider.data.model.NotificationDto
import com.app.namllahprovider.domain.repository.ConfigRepository
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

class NotificationApiImpl @Inject constructor(
    private val notificationApi: NotificationApi,
    private val configRepository: ConfigRepository

) {

    fun getAllNotification(pageNumber: Int): Maybe<List<NotificationDto>> = Maybe.create {
        val response = notificationApi.getAllNotification(pageNumber).execute()
        if (response.isSuccessful) {
            val notificationsResponse = response.body()
            Timber.tag(TAG).d("getAllNotification : notificationsResponse $notificationsResponse")
            if (notificationsResponse == null) {
                it.onError(Throwable("Something Error, Please try again later"))
            } else {
//                if (notificationsResponse.data.isEmpty()) {
//                    it.onComplete()
//                } else {
                    it.onSuccess(notificationsResponse.data)
//                }
            }
        } else {
            //Call on Error
            it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
        }
    }

    fun markNotificationAsRead(notificationId: Int): Maybe<BaseResponse> = Maybe.create {
        val response = notificationApi.markAsRead(notificationId).execute()
        if (response.isSuccessful) {
            val notificationsResponse = response.body()
            if (notificationsResponse == null) {
                it.onError(Throwable("Something Error, Please try again later"))
            } else {
                //Call on Success
                it.onSuccess(notificationsResponse)
            }
        } else {
            //Call on Error
            it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
        }
    }

    fun markAllNotificationAsRead(): Maybe<BaseResponse> = Maybe.create {
        val response = notificationApi.markAllAsRead().execute()
        if (response.isSuccessful) {
            val notificationsResponse = response.body()
            if (notificationsResponse == null) {
                it.onError(Throwable("Something Error, Please try again later"))
            } else {
                //Call on Success
                it.onSuccess(notificationsResponse)
            }
        } else {
            //Call on Error
            it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
        }
    }

    fun updateFCMToken(updateFCMTokenRequest: UpdateFCMTokenRequest): Maybe<BaseResponse> =
        Maybe.create {
            val response = notificationApi.updateFCMToken(
                mobile = updateFCMTokenRequest.mobile,
                token = updateFCMTokenRequest.fcmToken
            ).execute()
            if (response.isSuccessful) {
                val updateFCMTokenResponse = response.body()
                if (updateFCMTokenResponse == null) {
                    it.onError(Throwable("Something Error, Please try again later"))
                } else {
                    //Call on Success
                    it.onSuccess(updateFCMTokenResponse)
                }
            } else {
                //Call on Error
                it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
            }
        }

    companion object {
        private const val TAG = "NotificationApiImpl"
    }
}