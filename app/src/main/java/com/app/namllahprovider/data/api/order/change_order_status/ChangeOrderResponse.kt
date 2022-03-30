package com.app.namllahprovider.data.api.order.change_order_status

import com.app.namllahprovider.data.model.OrderDto
import com.google.gson.annotations.SerializedName

data class ChangeOrderResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("msg") val msg: String,
    @SerializedName("error") val error: String?,
    @SerializedName("data") val order: OrderDto?
)
