package com.app.namllahprovider.data.api.order.list_order

import com.app.namllahprovider.data.model.OrderDto
import com.google.gson.annotations.SerializedName

data class OrderData(
    @SerializedName("data") var orders: List<OrderDto>?
)