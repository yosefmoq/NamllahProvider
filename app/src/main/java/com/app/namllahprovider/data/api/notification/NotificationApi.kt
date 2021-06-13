package com.app.namllahprovider.data.api.notification

import com.app.namllahprovider.data.api.BaseResponse
import com.app.namllahprovider.data.api.notification.all_notification.NotificationsResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationApi {

    @GET("user/notifications")
    fun getAllNotification(
//        @Header("Authorization") token: String,
        @Query("page") pageNumber: Int
    ): Call<NotificationsResponse>

    @GET("user/notifications/{notificationId}/read")
    fun markAsRead(
//        @Header("Authorization") token: String,
        @Path("notificationId") notificationId: Int
    ): Call<BaseResponse>

    @GET("user/notifications/read-all")
    fun markAllAsRead(
//        @Header("Authorization") token: String,
    ): Call<BaseResponse>

}