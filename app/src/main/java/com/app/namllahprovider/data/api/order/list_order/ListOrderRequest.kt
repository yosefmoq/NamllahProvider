package com.app.namllahprovider.data.api.order.list_order

import com.app.namllahprovider.domain.utils.OrderType

data class ListOrderRequest(
    val orderType: OrderType
)
