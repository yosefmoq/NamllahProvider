package com.app.namllahprovider.data.api.auth.check_reset_password

import com.google.gson.annotations.SerializedName

data class CheckResetPasswordRequest(
    @SerializedName("mobile")
    val phoneNumber: String,

    @SerializedName("code")
    val code: String,
)
