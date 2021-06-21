package com.app.namllahprovider.data.api.notification.all_notification

import com.app.namllahprovider.data.model.NotificationDto
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class NotificationsResponse(

    @SerializedName("data") var data : List<NotificationDto>,
    @SerializedName("status") var status : Boolean,
    @SerializedName("msg") var msg : String
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
