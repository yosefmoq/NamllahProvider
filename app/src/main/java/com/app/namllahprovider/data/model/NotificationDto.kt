package com.app.namllahprovider.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


data class NotificationDto(
    @SerializedName("created_at") val created_at: String,
    @SerializedName("data") val data: NotificationDetailsDto,
    @SerializedName("id") val id: String,
    @SerializedName("notifiable_id") val notifiable_id: Int,
    @SerializedName("notifiable_type") val notifiable_type: String,
    @SerializedName("read_at") val read_at: String,
    @SerializedName("type") val type: String,
    @SerializedName("updated_at") val updated_at: String
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class NotificationDetailsDto(
    @SerializedName("order_id") var orderId: Int,
    @SerializedName("duration") var duration: Int,
    @SerializedName("type") var type: String,
    @SerializedName("total_price") var total_price: String

){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class Links(
    @SerializedName("url") var url: String,
    @SerializedName("label") var label: String,
    @SerializedName("active") var active: Boolean
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class NotificationMetaDto(

    @SerializedName("current_page") var currentPage: Int,
    @SerializedName("from") var from: Int,
    @SerializedName("last_page") var lastPage: Int,
    @SerializedName("links") var links: List<Links>,
    @SerializedName("path") var path: String,
    @SerializedName("per_page") var perPage: Int,
    @SerializedName("to") var to: Int,
    @SerializedName("total") var total: Int

){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}