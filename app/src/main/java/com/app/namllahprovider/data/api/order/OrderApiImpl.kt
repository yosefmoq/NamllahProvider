package com.app.namllahprovider.data.api.order

import com.app.namllahprovider.data.api.order.change_order_status.ChangeOrderRequest
import com.app.namllahprovider.data.api.order.change_order_status.ChangeOrderResponse
import com.app.namllahprovider.data.api.order.list_order.ListOrderRequest
import com.app.namllahprovider.data.api.order.show_order.ShowOrderRequest
import com.app.namllahprovider.data.model.OrderDto
import com.app.namllahprovider.domain.repository.ConfigRepository
import com.app.namllahprovider.domain.utils.OrderStatusRequestType
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
//                    if (listOrderResponse.orders?.isEmpty()!!) {
//                        //Call on Complete
//                        it.onComplete()
//                    } else {
                    //Call on Success
                    it.onSuccess(listOrderResponse.orders ?: listOf())
//                    }
                }
            } else {
                //Call on Error
                Timber.tag(TAG).e(Throwable(response.errorBody()?.string() ?: "Something went wrong!"),"getListOrder : ")
                it.onError(Throwable(response.errorBody()?.string() ?: "Something went wrong!"))
            }
        }

    fun changeOrderStatus(changeOrderRequest: ChangeOrderRequest): Maybe<ChangeOrderResponse> =
        Maybe.create {
            Timber.tag(TAG).d("changeOrderStatus : changeOrderRequest $changeOrderRequest")
            val response = when (changeOrderRequest.orderStatusRequestType) {
                OrderStatusRequestType.ADD_BILLS -> {
                    orderApi.changeOrderStatusPOST2(
                        orderId = changeOrderRequest.orderId,
                        orderStatus = changeOrderRequest.orderStatusRequestType.status,
                        bringTimes = changeOrderRequest.bringTimes!!,
                        boughtPrice = changeOrderRequest.boughtPrice!!,
                        bills = changeOrderRequest.bills ?: listOf()
                    )
                }
                OrderStatusRequestType.CANCEL -> {
                    orderApi.changeOrderStatusPOST4(
                        orderId = changeOrderRequest.orderId,
                        orderStatus = changeOrderRequest.orderStatusRequestType.status,
                        cancelReasonId = changeOrderRequest.cancelReasonId
                    )
                }
                OrderStatusRequestType.PAY_ORDER -> {
                    orderApi.changeOrderStatusPOST3(
                        orderId = changeOrderRequest.orderId,
                        orderStatus = changeOrderRequest.orderStatusRequestType.status,
                        amount = changeOrderRequest.amount
                    )
                }
                OrderStatusRequestType.CHECK -> {
                    orderApi.changeOrderStatusPOST(
                        orderId = changeOrderRequest.orderId,
                        orderStatus = changeOrderRequest.orderStatusRequestType.status,
                        estimatedTime = changeOrderRequest.estimatedTime,
                        estimatedPriceParts = changeOrderRequest.estimatedPriceParts.toInt(),
                        checkDescription = changeOrderRequest.checkDescription,
                    )
                }
                else -> {
                    orderApi.changeOrderStatusGET(
                        orderId = changeOrderRequest.orderId,
                        orderStatus = changeOrderRequest.orderStatusRequestType.status
                    )
                }
            }.execute()
            Timber.tag(TAG).d("changeOrderStatus : response $response")
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
                    it.onSuccess(listOrderResponse.order!!)
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