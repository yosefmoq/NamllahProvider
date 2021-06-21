package com.app.namllahprovider.data.api.order.show_order

import com.app.namllahprovider.data.model.OrderDto
import com.google.gson.annotations.SerializedName

data class ShowOrderResponse(
    @SerializedName("status") var status: Boolean,
    @SerializedName("msg") var msg: String,
    @SerializedName("data") val order: OrderDto?
)
