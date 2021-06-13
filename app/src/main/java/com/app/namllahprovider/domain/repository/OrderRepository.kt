package com.app.namllahprovider.domain.repository

import com.app.namllahprovider.data.api.order.change_order_status.ChangeOrderRequest
import com.app.namllahprovider.data.api.order.change_order_status.ChangeOrderResponse
import com.app.namllahprovider.data.api.order.list_order.ListOrderRequest
import com.app.namllahprovider.data.api.order.show_order.ShowOrderRequest
import com.app.namllahprovider.data.model.OrderDto
import io.reactivex.Maybe

interface OrderRepository {

    fun getListOrder(listOrderRequest: ListOrderRequest): Maybe<List<OrderDto>>

    fun changeOrderStatus(changeOrderRequest: ChangeOrderRequest): Maybe<ChangeOrderResponse>

    fun showOrder(showOrderRequest: ShowOrderRequest): Maybe<OrderDto>
}