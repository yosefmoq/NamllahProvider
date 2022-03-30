package com.app.namllahprovider.data.model

import com.google.gson.Gson

data class ErrorsDto(
    val mobile: List<String>? = null,
    val password: List<String>? = null,
    val status: List<String>? = null
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}