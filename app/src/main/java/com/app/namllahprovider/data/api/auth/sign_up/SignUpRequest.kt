package com.app.namllahprovider.data.api.auth.sign_up

import com.google.gson.annotations.SerializedName

data class SignUpRequest(
    @SerializedName("mobile")
    private val phoneNumber: String,
    @SerializedName("password")
    private val password: String,
    @SerializedName("language")
    private val language: String,
)