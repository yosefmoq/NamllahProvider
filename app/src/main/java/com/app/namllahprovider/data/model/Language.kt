package com.app.namllahprovider.data.model

import com.google.gson.Gson

data class Language (
    val id: Long,
    val code: String,
    val name: String
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}