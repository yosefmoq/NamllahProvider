package com.app.namllahprovider.data.api.order

import com.app.namllahprovider.data.api.order.change_order_status.ChangeOrderResponse
import com.app.namllahprovider.data.api.order.list_order.ListOrderResponse
import com.app.namllahprovider.data.api.order.show_order.ShowOrderResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface OrderApi {

    @GET("provider/orders")
    fun getListOrders(
//        @Header("Authorization") token: String,
        @Query("status") status: String
    ): Call<ListOrderResponse>

    @GET("provider/orders/{orderId}/{orderStatus}")
    fun changeOrderStatusGET(
//        @Header("Authorization") token: String,
        @Path("orderId") orderId: Int,
        @Path("orderStatus") orderStatus: String
    ): Call<ChangeOrderResponse>

    @FormUrlEncoded
    @POST("provider/orders/{orderId}/{orderStatus}")
    fun changeOrderStatusPOST(
        @Path("orderId") orderId: Int,
        @Path("orderStatus") orderStatus: String,
        @Field("estimated_time") estimatedTime: Int,
        @Field("estimated_price_parts") estimatedPriceParts: Int,
        @Field("check_description") checkDescription: String,
    ): Call<ChangeOrderResponse>

    @Multipart
    @POST("provider/orders/{orderId}/{orderStatus}")
    fun changeOrderStatusPOST2(
        @Path("orderId") orderId: Int,
        @Path("orderStatus") orderStatus: String,
        @Part("bought_price") boughtPrice: RequestBody,
        @Part("bring_times") bringTimes: RequestBody,
        @Part bills: List<MultipartBody.Part>
    ): Call<ChangeOrderResponse>

    @FormUrlEncoded
    @POST("provider/orders/{orderId}/{orderStatus}")
    fun changeOrderStatusPOST3(
        @Path("orderId") orderId: Int,
        @Path("orderStatus") orderStatus: String,
        @Field("amount") amount: Int,
    ): Call<ChangeOrderResponse>

    @FormUrlEncoded
    @POST("provider/orders/{orderId}/{orderStatus}")
    fun changeOrderStatusPOST4(
        @Path("orderId") orderId: Int,
        @Path("orderStatus") orderStatus: String,
        @Field("cancel_reason_id") cancelReasonId: Int,
    ): Call<ChangeOrderResponse>

    @GET("provider/orders/{orderId}")
    fun showOrder(
//        @Header("Authorization") token: String,
        @Path("orderId") orderId: Int,
    ): Call<ShowOrderResponse>
}