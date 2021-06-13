package com.app.namllahprovider.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("id") var id: Int,
    @SerializedName("mobile") var mobile: String,
    @SerializedName("new_mobile") var newMobile: String,
    @SerializedName("settings") var settings: SettingsDto,
    @SerializedName("images") var images: String,
    @SerializedName("name") var name: String,
    @SerializedName("language") var language: LanguageDto,
    @SerializedName("type") var type: String,
    @SerializedName("status") var status: Int,
    @SerializedName("is_complete") var isComplete: Int,
    @SerializedName("services") var services: ServicesDto,
    @SerializedName("lat") var lat: String,
    @SerializedName("lng") var lng: String,
    @SerializedName("rate") var rate: Int,
    @SerializedName("wallet") var wallet: String,
    @SerializedName("is_available") var isAvailable: Int,
    @SerializedName("token") val token: String? = null
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
data class LanguageDto (
    val id: Long,
    val code: String,
    val name: String
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class SettingsDto (
    val notification: String
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}




