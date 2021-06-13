package com.app.namllahprovider.data.api.order.show_order

import com.app.namllahprovider.data.model.OrderDto

data class ShowOrderResponse(
    val status: Boolean,
    val msg: String,
    val order: OrderDto?
)
