package com.app.namllahprovider.data.api.auth.sign_up

import com.app.namllahprovider.data.model.ErrorsDto
import com.google.gson.annotations.SerializedName


data class SignUpResponse(
    @SerializedName("status")
    val status: Boolean?,
    val message: String?,
    val code: String?,
    val errors: ErrorsDto? = null
)