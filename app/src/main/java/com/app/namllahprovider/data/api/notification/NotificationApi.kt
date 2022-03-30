package com.app.namllahprovider.data.api.notification

import com.app.namllahprovider.data.api.BaseResponse
import com.app.namllahprovider.data.api.notification.all_notification.NotificationsResponse
import retrofit2.Call
import retrofit2.http.*

interface NotificationApi {

    @GET("user/notifications")
    fun getAllNotification(
//        @Header("Authorization") token: String,
        @Query("page") pageNumber: Int
    ): Call<NotificationsResponse>

    @GET("user/notifications/{notificationId}/read")
    fun markAsRead(
        @Path("notificationId") notificationId: Int
    ): Call<BaseResponse>

    @GET("user/notifications/read-all")
    fun markAllAsRead(): Call<BaseResponse>

    @FormUrlEncoded
    @POST("user/notifications/update-firebase")
    fun updateFCMToken(
        @Field("mobile") mobile: String,
        @Field("token") token: String
    ): Call<BaseResponse>

}