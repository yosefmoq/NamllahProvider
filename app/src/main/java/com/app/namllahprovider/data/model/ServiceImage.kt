package com.app.namllahprovider.data.model

import com.google.gson.Gson


data class ServiceImage (
    val original: String
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}
