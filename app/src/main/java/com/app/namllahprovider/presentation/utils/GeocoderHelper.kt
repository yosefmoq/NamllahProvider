package com.app.namllahprovider.presentation.utils

import android.content.Context
import android.location.Geocoder

fun getAddressFromLatAndLng(context: Context, lat: Double, lng: Double): String {
    val geocoder = Geocoder(context)
    //Gaza lat 31.49 lng 34.45
    //Medina Saudi Arabia lat 24.5511 lng 39.6711
    val list = geocoder.getFromLocation(lat, lng, 1)
    val address = list[0].getAddressLine(0).split(",")
    println("address getAddressFromLatAndLng $address")
    return "${address[0]} - ${address[1]}"
}