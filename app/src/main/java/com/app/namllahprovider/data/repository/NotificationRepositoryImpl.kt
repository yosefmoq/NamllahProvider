package com.app.namllahprovider.data.repository

import com.app.namllahprovider.data.api.BaseResponse
import com.app.namllahprovider.data.api.notification.NotificationApiImpl
import com.app.namllahprovider.data.api.notification.update_fcm.UpdateFCMTokenRequest
import com.app.namllahprovider.data.model.NotificationDto
import com.app.namllahprovider.domain.repository.NotificationRepository
import io.reactivex.Maybe
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val notificationApiImpl: NotificationApiImpl
) : NotificationRepository {

    override fun getAllNotification(pageNumber: Int): Maybe<List<NotificationDto>> =
        notificationApiImpl.getAllNotification(pageNumber)

    override fun markNotificationAsRead(notificationId: Int): Maybe<BaseResponse> =
        notificationApiImpl.markNotificationAsRead(notificationId)

    override fun markAllNotificationAsRead(): Maybe<BaseResponse> =
        notificationApiImpl.markAllNotificationAsRead()

    override fun updateFCMToken(updateFCMTokenRequest: UpdateFCMTokenRequest): Maybe<BaseResponse> =
        notificationApiImpl.updateFCMToken(updateFCMTokenRequest)

}