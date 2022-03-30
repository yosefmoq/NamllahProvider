package com.app.namllahprovider.data.api.user.change_available

import com.app.namllahprovider.data.model.ErrorsDto

data class ChangeAvailableResponse(
    val status: Boolean,
    val msg: String?,
    val message: String?,
    val errors: ErrorsDto?,
)