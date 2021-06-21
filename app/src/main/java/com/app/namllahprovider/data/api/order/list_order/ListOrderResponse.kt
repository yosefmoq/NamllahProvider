package com.app.namllahprovider.data.api.order.list_order

import com.app.namllahprovider.data.model.OrderDto
import com.google.gson.annotations.SerializedName

data class ListOrderResponse2(
    val status: Boolean,
    val msg: String,
    @SerializedName("data")
    val orders: List<OrderDto>
)
