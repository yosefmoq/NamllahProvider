package com.app.namllahprovider.data.api.auth.sign_in

import com.app.namllahprovider.data.model.UserDto
import com.google.gson.annotations.SerializedName

data class SignInResponse(
    @SerializedName("data") var userDto: UserDto?,
    @SerializedName("status") var status: Boolean? = false,
    @SerializedName("error") var error: String? = "",
    @SerializedName("msg") var msg: String? = ""
)
