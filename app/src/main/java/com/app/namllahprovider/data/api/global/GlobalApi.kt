package com.app.namllahprovider.data.api.global

import com.app.namllahprovider.data.model.ServicesDto
import retrofit2.Call
import retrofit2.http.GET

interface GlobalApi {

    @GET("services")
    fun getServices(): Call<ServicesDto>


    @GET("areas")
    fun getAreas(): Call<GetAreasResponse>
}