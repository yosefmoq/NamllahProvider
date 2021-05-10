package com.app.namllahprovider.data.api.auth.sign_up

import com.google.gson.annotations.SerializedName


data class SignUpResponse(
    @SerializedName("status")
    val status: Boolean?,
    val msg: String?,
    val code: String?,
)