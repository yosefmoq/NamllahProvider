package com.app.namllahprovider.data.model

import com.google.gson.Gson

data class Settings (
    val notification: String
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}