package com.app.namllahprovider.data.api.order.change_order_status

import com.app.namllahprovider.domain.utils.OrderStatusRequestType
import okhttp3.MultipartBody
import okhttp3.RequestBody

data class ChangeOrderRequest(
    val orderId: Int,
    val orderStatusRequestType: OrderStatusRequestType,
    val estimatedTime: Double = 0.0,
    val amount: Int = 0,
    val cancelReasonId: Int = 0,
    val estimatedPriceParts: Double = 0.0,
    val checkDescription: String = "",
    val boughtPrice: RequestBody? = null,
    val bringTimes: RequestBody? = null,
    val bills: List<MultipartBody.Part>? = listOf()
)