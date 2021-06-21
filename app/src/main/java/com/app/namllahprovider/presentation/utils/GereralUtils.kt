package com.app.namllahprovider.presentation.utils

import com.app.namllahprovider.data.model.StatusDto
import java.util.concurrent.TimeUnit

fun StatusDto.getOrderStatus() =
    OrderStat.getOrderStatById(this.id)


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

fun Int.getTime(): String {
    return String.format(
        "%02d:%02d:%02d",
        TimeUnit.MILLISECONDS.toHours(this.toLong()),
        TimeUnit.MILLISECONDS.toMinutes(this.toLong()) -
                TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(this.toLong())), // The change is in this line
        TimeUnit.MILLISECONDS.toSeconds(this.toLong()) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(this.toLong()))
    )
}