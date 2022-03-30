package com.app.namllahprovider.data.api.auth.forget_password

import com.app.namllahprovider.data.model.ErrorsDto

data class ForgetPasswordResponse(
    val status: Boolean,
    val error: String?,
    val msg: String?,
    val code: String?,
    val errors: ErrorsDto? = null
)
