package com.app.namllahprovider.data.api.order.change_order_status

import com.app.namllahprovider.data.model.OrderDto

data class ChangeOrderResponse(
    val status: Boolean,
    val msg: String,
    val error:String?,
    val order: OrderDto?
)
