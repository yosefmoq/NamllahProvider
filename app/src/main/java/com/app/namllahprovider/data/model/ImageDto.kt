package com.app.namllahprovider.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

data class ImageDto(
    @SerializedName("thump") var thump: String?,
    @SerializedName("low") var low: String?,
    @SerializedName("med") var med: String?,
    @SerializedName("high") var high: String?,
    @SerializedName("original") var original: String?
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}