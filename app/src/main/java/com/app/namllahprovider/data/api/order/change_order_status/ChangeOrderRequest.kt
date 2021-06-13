package com.app.namllahprovider.data.api.order.change_order_status

import com.app.namllahprovider.domain.utils.OrderStatus

data class ChangeOrderRequest(val orderId: Int, val orderStatus: OrderStatus)