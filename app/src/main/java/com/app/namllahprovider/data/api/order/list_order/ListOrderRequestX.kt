package com.app.namllahprovider.data.api.order.list_order

import com.app.namllahprovider.data.model.OrderDto
import com.google.gson.annotations.SerializedName

data class ListOrderResponse(
    @SerializedName("status") var status : Boolean,
    @SerializedName("msg") var msg : String,
    @SerializedName("data") var orders: List<OrderDto>?
)