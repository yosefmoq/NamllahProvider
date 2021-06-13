package com.app.namllahprovider.data.api.order

import com.app.namllahprovider.data.api.order.change_order_status.ChangeOrderRequest
import com.app.namllahprovider.data.api.order.change_order_status.ChangeOrderResponse
import com.app.namllahprovider.data.api.order.list_order.ListOrderRequest
import com.app.namllahprovider.data.api.order.show_order.ShowOrderRequest
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.domain.repository.ConfigRepository
import com.app.namllahprovider.domain.utils.OrderStatus
import io.reactivex.Maybe
import timber.log.Timber
import javax.inject.Inject

class OrderApiImpl @Inject constructor(
    private val orderApi: OrderApi,
    private val configRepository: ConfigRepository
) {

    fun getListOrder(listOrderRequest: ListOrderRequest): Maybe<List<OrderDto>> =
        Maybe.create {
            val response = orderApi.getListOrders(
//                token =  "Bearer " + configRepository.getUserToken()
                status = listOrderRequest.orderType.type
            ).execute()
            if (response.isSuccessful) {
                val listOrderResponse = response.body()
                if (listOrderResponse == null) {
                    it.onError(Throwable("Something Error, Please try again later"))
                } else {
                    if (listOrderResponse.orders.isEmpty()) {
                        //Call on Complete
                        it.onComplete()
                    } else {
                        //Call on Success
                        it.onSuccess(listOrderResponse.orders)
                    }
                }
            } else {
                //Call on Error
                it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
            }
        }

    fun changeOrderStatus(changeOrderRequest: ChangeOrderRequest): Maybe<ChangeOrderResponse> =
        Maybe.create {

            val response =
                if (changeOrderRequest.orderStatus == OrderStatus.CHECK || changeOrderRequest.orderStatus == OrderStatus.PAY_ORDER) {
                    orderApi.changeOrderStatusPOST(
//                        token =  "Bearer " + configRepository.getLoggedUser()?.token ?: "",
                        orderId = changeOrderRequest.orderId,
                        orderStatus = changeOrderRequest.orderStatus.status
                    )
                } else {
                    orderApi.changeOrderStatusGET(
//                        token =  "Bearer " + configRepository.getLoggedUser()?.token ?: "",
                        orderId = changeOrderRequest.orderId,
                        orderStatus = changeOrderRequest.orderStatus.status
                    )
                }.execute()
            if (response.isSuccessful) {
                val changeOrderResponse = response.body()
                if (changeOrderResponse == null) {
                    it.onError(Throwable("Something Error, Please try again later"))
                } else {
                    //Call on Success
                    it.onSuccess(changeOrderResponse)
                }
            } else {
                //Call on Error
                it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
            }
        }

    fun showOrder(showOrderRequest: ShowOrderRequest): Maybe<OrderDto> =
        Maybe.create {
            val response = orderApi.showOrder(
//                token = "Bearer " + configRepository.getLoggedUser()?.token ?: "",
                orderId = showOrderRequest.orderId
            ).execute()
            if (response.isSuccessful) {
                val listOrderResponse = response.body()
                if (listOrderResponse == null) {
                    it.onError(Throwable("Something Error, Please try again later"))
                } else {
                    //Call on Success
                    it.onSuccess(listOrderResponse.order ?: OrderDto())
                }
            } else {
                //Call on Error
                it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
            }
        }

    companion object {
        private const val TAG = "OrderApiImpl"
    }
}