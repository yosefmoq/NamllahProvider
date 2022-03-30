package com.app.namllahprovider.domain.repository

import com.app.namllahprovider.data.api.BaseResponse
import com.app.namllahprovider.data.api.notification.update_fcm.UpdateFCMTokenRequest
import com.app.namllahprovider.data.model.NotificationDto
import io.reactivex.Maybe

interface NotificationRepository {

    fun getAllNotification(pageNumber :Int): Maybe<List<NotificationDto>>

    fun markNotificationAsRead(notificationId: Int): Maybe<BaseResponse>

    fun markAllNotificationAsRead(): Maybe<BaseResponse>

    fun updateFCMToken(updateFCMTokenRequest:UpdateFCMTokenRequest): Maybe<BaseResponse>
}