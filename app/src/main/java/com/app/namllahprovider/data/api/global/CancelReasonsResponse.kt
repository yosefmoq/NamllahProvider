package com.app.namllahprovider.data.api.global

import com.app.namllahprovider.data.model.CancelReasonDto
import com.google.gson.annotations.SerializedName

data class CancelReasonsResponse(
    @SerializedName("status") val status: Boolean? = null,
    @SerializedName("msg") val msg: String? = null,
    @SerializedName("data") val cancelReasonsList: List<CancelReasonDto>? = null
)