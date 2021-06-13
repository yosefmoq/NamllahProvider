package com.app.namllahprovider.data.api.user.update_user_profile

import com.app.namllahprovider.data.model.ErrorsDto
import com.app.namllahprovider.data.model.UserDto
import com.google.gson.annotations.SerializedName

data class UpdateUserProfileResponse(
    @SerializedName("data")
    val user: UserDto?,
    val status: Boolean,
    val error: String?,
    val msg: String?,
    val errors: ErrorsDto?
)