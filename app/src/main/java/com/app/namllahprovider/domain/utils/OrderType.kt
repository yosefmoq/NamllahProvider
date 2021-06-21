package com.app.namllahprovider.domain.utils

enum class OrderType(val type: String) {
    NEW("new"),
    IN_PROGRESS("in_progress"),
    FINISHED("finished")
}

enum class OrderStatusRequestType(processSeq: Int, val status: String, val label: String? = "") {
    WAITING(1, ""),
    APPROVED(2, "approve", "Approving"),
    IN_WAY(3, "in-way"),
    ARRIVE(4, "arrive"),
    START_CHECK(5, "start-check"),
    CHECK(5, "check"),
    START_WORK(6, "start-work"),

    STOP_WORK(6, "stop-work"),
    COMPLETE(7, ""),

    FINISH_ORDER(10, "finish"),
    PAY_ORDER(11, "pay"),

    CANCEL(8, "", "Canceling");

    companion object {

        fun getOrderStatusById(id: Int) =
            when (id) {
                1 -> WAITING
                2 -> APPROVED
                3 -> IN_WAY
                4 -> ARRIVE
                5 -> START_CHECK
                6 -> START_WORK
                7 -> COMPLETE
                8 -> CANCEL
                else -> WAITING
            }
    }

}


