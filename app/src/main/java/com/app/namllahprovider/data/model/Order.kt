package com.app.namllahprovider.data.model

import com.google.gson.Gson

data class Order(
    val id: Int,
    val title: String,
    val imageUrl: String = "https://i.picsum.photos/id/168/200/300.jpg?hmac=ILU5dddz6ohoQEq3_1fmoy2wEFfM1V1JfjLX_JsbOz0"
){
    override fun toString(): String {
        return Gson().toJson(this)
    }
}