package com.app.namllahprovider.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class Services (
    @SerializedName("data")
    val servicesList: List<ServiceDto>
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}