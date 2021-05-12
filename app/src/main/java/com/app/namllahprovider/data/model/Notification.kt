package com.app.namllahprovider.data.model

import android.accounts.AuthenticatorDescription

data class Notification(
    val id: Int,
    val name: String,
    val description: String,
    val imageUrl: String = "https://i.picsum.photos/id/948/200/200.jpg?hmac=h64Z3zl6jLB_DtaWe83fhSQY-r_Sum7pndIJrZZ9rtQ"
)