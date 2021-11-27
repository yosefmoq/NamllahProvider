package com.app.namllahprovider.presentation.utils

import android.util.Log
import com.app.namllahprovider.data.model.StatusDto
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*
import java.util.concurrent.TimeUnit

fun StatusDto.getOrderStatus() =
    OrderStat.getOrderStatById(this.id)

fun Long.getDifferance(): Long {
    //    val interval = Interval(this, c.timeInMillis/1000)
    return if (this == 0L)
        0
    else {
        (testTimeZone() / 1000) - this
    }
}

fun getUserDocument(id: String): DocumentReference {
    val firestore = FirebaseFirestore.getInstance()
    return firestore.collection("Users").document(id)
}

private fun testTimeZone(): Long {
    val tz = TimeZone.getTimeZone("GMT+03:00");
    val c = Calendar.getInstance(tz);
    val time = String.format("%02d", c.get(Calendar.HOUR_OF_DAY)) + ":" +
            String.format("%02d", c.get(Calendar.MINUTE)) + ":" +
            String.format("%02d", c.get(Calendar.SECOND)) + ":" +
            String.format("%03d", c.get(Calendar.MILLISECOND));
    Log.v("tttt", time)
    Log.v("ttt", c.timeInMillis.toString())
    return c.timeInMillis
}


fun String.toDate(): Long {


    if (this.isEmpty()) {
        return 0
    } else {
        val year = this.split(" ")[0].split("-")[0].toInt()
        val month = this.split(" ")[0].split("-")[1].toInt()
        val day = this.split(" ")[0].split("-")[2].toInt()
        val hour = this.split(" ")[1].split(":")[0].toInt()
        val min = this.split(" ")[1].split(":")[1].toInt()
        val sec = this.split(" ")[1].split(":")[2].toInt()

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.YEAR, year)
        calendar.set(Calendar.MONTH, month)
        calendar.set(Calendar.DAY_OF_MONTH, day)
        if (hour > 12) {
            calendar.set(Calendar.HOUR_OF_DAY, hour)

        } else {
            calendar.set(Calendar.HOUR, hour)

        }
        calendar.set(Calendar.MINUTE, min)
        calendar.set(Calendar.SECOND, sec)
        return calendar.timeInMillis
    }
}

enum class OrderStat(val id: Int) {
    WAITING(1),
    APPROVED(2),
    IN_WAY(3),
    ARRIVE(4),
    CHECK(5),
    WORKING(6),
    COMPLETE(7),
    CANCEL(8);

    companion object {
        fun getOrderStatById(id: Int) =
            when (id) {
                1 -> WAITING
                2 -> APPROVED
                3 -> IN_WAY
                4 -> ARRIVE
                5 -> CHECK
                6 -> WORKING
                7 -> COMPLETE
                8 -> CANCEL
                else -> WAITING
            }

    }
}

fun Long.getTime(): String {
    return String.format(
        "%02d:%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(this.toLong()),
        TimeUnit.MILLISECONDS.toMinutes(this.toLong()) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this.toLong())), // The change is in this line
        TimeUnit.MILLISECONDS.toSeconds(this.toLong()) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.toLong()))
    )
}

