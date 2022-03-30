package com.app.namllahprovider.data.api

import com.google.gson.annotations.SerializedName

data class BaseResponse(
    @SerializedName("status") val status: Boolean? = null,
    @SerializedName("msg") val msg: String? = null
)
