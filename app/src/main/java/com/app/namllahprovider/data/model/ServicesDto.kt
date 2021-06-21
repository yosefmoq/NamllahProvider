package com.app.namllahprovider.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ServicesDto(
    @SerializedName("data")
    val servicesList: List<ServiceDto>
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class ServiceDto(
    @SerializedName("id") var id: Int,
    @SerializedName("image") var image: ImageDto,
    @SerializedName("title") var title: String,
    @SerializedName("description") var description: String
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}


