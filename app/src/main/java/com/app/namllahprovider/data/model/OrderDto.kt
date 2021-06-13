package com.app.namllahprovider.data.model

import com.google.gson.Gson


data class OrderDto(
    val area: AreaDto? = null,
    val bills: List<Any?>? = null,
    val discount: String? = null,
    val duration: Long? = null,
    val id: Long? = null,
    val lat: String? = null,
    val lng: String? = null,
    val payment: PaymentDto? = null,
    val price: Long? = null,
    val provider: ProviderDto? = null,
    val status: StatusDto? = null,
    val serviceDto: ServiceDto? = null,
    val user: CustomerDto? = null
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class AreaDto(
    val id: Long,
    val title: String,
    val lat: String,
    val lng: String,
    val diameter: Long
)

data class PaymentDto(
    val id: Long,
    val title: String,
    val description: String,
    val code: String
)

data class ProviderDto(
    val id: Long,
    val name: String,
    val mobile: String
)

data class StatusDto(
    val id: Long,
    val title: String
)

data class CustomerDto(
    val id: Long,
    val name: String,
    val mobile: String
)