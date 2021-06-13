package com.app.namllahprovider.data.api.order

import com.app.namllahprovider.data.api.order.change_order_status.ChangeOrderResponse
import com.app.namllahprovider.data.api.order.list_order.ListOrderResponse
import com.app.namllahprovider.data.api.order.show_order.ShowOrderResponse
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

    @POST("provider/orders/{orderId}/{orderStatus}")
    fun changeOrderStatusPOST(
//        @Header("Authorization") token: String,
        @Path("orderId") orderId: Int,
        @Path("orderStatus") orderStatus: String
    ): Call<ChangeOrderResponse>

    @GET("provider/orders/{orderId}")
    fun showOrder(
//        @Header("Authorization") token: String,
        @Path("orderId") orderId: Int,
    ): Call<ShowOrderResponse>
}