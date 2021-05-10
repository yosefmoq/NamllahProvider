package com.app.namllahprovider.data.api.auth.verification_code

import com.app.namllahprovider.data.model.User
import com.google.gson.annotations.SerializedName

data class VerificationCodeResponse(
    @SerializedName("data")
    var user: User?,
    @SerializedName("status")
    val status: Boolean?,
    @SerializedName("msg")
    val msg: String?,
    @SerializedName("error")
    val error: String?,
)

