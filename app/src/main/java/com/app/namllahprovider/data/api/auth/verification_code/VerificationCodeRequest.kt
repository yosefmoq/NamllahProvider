package com.app.namllahprovider.data.api.auth.verification_code

import com.google.gson.annotations.SerializedName

data class VerificationCodeRequest(
    @SerializedName("mobile")
    val phoneNumber: String,
    @SerializedName("code")
    val code: String,
)