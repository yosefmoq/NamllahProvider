package com.app.namllahprovider.data.repository

import com.app.namllahprovider.data.api.order.OrderApiImpl
import com.app.namllahprovider.data.api.order.change_order_status.ChangeOrderRequest
import com.app.namllahprovider.data.api.order.change_order_status.ChangeOrderResponse
import com.app.namllahprovider.data.api.order.list_order.ListOrderRequest
import com.app.namllahprovider.data.api.order.show_order.ShowOrderRequest
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.domain.repository.OrderRepository
import io.reactivex.Maybe
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(
    private val orderApiImpl: OrderApiImpl
) : OrderRepository {

    override fun getListOrder(listOrderRequest: ListOrderRequest): Maybe<List<OrderDto>> =
        orderApiImpl.getListOrder(listOrderRequest)

    override fun changeOrderStatus(changeOrderRequest: ChangeOrderRequest): Maybe<ChangeOrderResponse> =
        orderApiImpl.changeOrderStatus(changeOrderRequest)

    override fun showOrder(showOrderRequest: ShowOrderRequest): Maybe<OrderDto>  =
        orderApiImpl.showOrder(showOrderRequest)
}