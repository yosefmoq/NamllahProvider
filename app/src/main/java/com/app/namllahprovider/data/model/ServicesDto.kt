package com.app.namllahprovider.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ServicesDto (
    @SerializedName("data")
    val servicesList: List<ServiceDto>
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class ServiceDto (
    val id: Long,
    val serviceImage: ServiceImageDto,
    val title: String,
    val description: String
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class ServiceImageDto (
    val original: String
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
