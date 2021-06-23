package com.app.namllahprovider.data.model

import com.google.gson.annotations.SerializedName

data class CancelReasonDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String
)