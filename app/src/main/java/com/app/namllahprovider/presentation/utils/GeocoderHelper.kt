package com.app.namllahprovider.presentation.utils

import android.content.Context
import android.location.Geocoder

fun getAddressFromLatAndLng(context: Context, lat: Double, lng: Double): String {

    return ""
/*
    val geocoder = Geocoder(context)
    //Gaza lat 31.49 lng 34.45
    //Medina Saudi Arabia lat 24.5511 lng 39.6711
    val list = geocoder.getFromLocation(lat, lng, 1)
    return try {
        val address = list[0].getAddressLine(0).split(",")
        println("address getAddressFromLatAndLng $address")
        "${address[0]} - ${address[1]}"
    } catch (e: Exception) {
        ""
    }

*/
}