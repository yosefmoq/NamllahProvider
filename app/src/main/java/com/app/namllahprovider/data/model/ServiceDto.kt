package com.app.namllahprovider.data.model

import com.google.gson.Gson

data class ServiceDto (
    val id: Long,
    val serviceImage: ServiceImage,
    val title: String,
    val description: String
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}