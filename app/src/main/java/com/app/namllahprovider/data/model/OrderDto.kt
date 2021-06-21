package com.app.namllahprovider.data.model

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName


data class OrderDto(
    @SerializedName("id") var id: Int? ,
    @SerializedName("user") var user: CustomerDto? ,
    @SerializedName("provider") var provider: ProviderDto? ,
    @SerializedName("status") var status: StatusDto? ,
    @SerializedName("payment") var payment: PaymentDto? ,
    @SerializedName("area") var area: AreaDto? ,
    @SerializedName("service") var service: ServiceDto? ,
    @SerializedName("check_at") var checkAt: String? ,
    @SerializedName("estimated_time") var estimatedTime: Int? ,
    @SerializedName("estimated_price_parts") var estimatedPriceParts: Int? ,
    @SerializedName("estimated_price") var estimatedPrice: Int? ,
    @SerializedName("check_description") var checkDescription: String? ,
    @SerializedName("is_pay_complete") var isPayComplete: Int? ,
    @SerializedName("cancel_reason_id") var cancelReasonId: String? ,
    @SerializedName("cancel_reason") var cancelReason: String? ,
    @SerializedName("cancel_at") var cancelAt: String? ,
    @SerializedName("is_cancel") var isCancel: Int? ,
    @SerializedName("cancel_by_me") var cancelByMe: Int? ,
    @SerializedName("duration") var duration: Int? ,
    @SerializedName("is_working") var isWorking: Int? ,
    @SerializedName("start_at") var startAt: String? ,
    @SerializedName("complete_at") var completeAt: String? ,
    @SerializedName("bring_times") var bringTimes: Int? ,
    @SerializedName("bought_price") var boughtPrice: Int? ,
    @SerializedName("price") var price: Double? ,
    @SerializedName("discount") var discount: String? ,
    @SerializedName("total_price") var totalPrice: Double? ,
    @SerializedName("total_price_floor") var totalPriceFloor: Double? ,
    @SerializedName("hour_price") var hourPrice: Int? ,
    @SerializedName("provider_profit") var providerProfit: Double? ,
    @SerializedName("total_provider_money") var totalProviderMoney: Double? ,
    @SerializedName("lat") var lat: Double? ,
    @SerializedName("lng") var lng: Double? ,
    @SerializedName("created_at") var createdAt: String? ,
    @SerializedName("bills") var bills: List<String>?
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class AreaDto(
    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("lat") var lat: String,
    @SerializedName("lng") var lng: String,
    @SerializedName("diameter") var diameter: Int
)

data class PaymentDto(
    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String,
    @SerializedName("description") var description: String,
    @SerializedName("code") var code: String

)

data class ProviderDto(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("mobile") var mobile: String
)

data class StatusDto(

    @SerializedName("id") var id: Int,
    @SerializedName("title") var title: String
)

data class CustomerDto(
    @SerializedName("id") var id: Int,
    @SerializedName("name") var name: String,
    @SerializedName("mobile") var mobile: String
)