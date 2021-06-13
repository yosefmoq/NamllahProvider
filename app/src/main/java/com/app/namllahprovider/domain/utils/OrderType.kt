package com.app.namllahprovider.domain.utils

enum class OrderType(val type: String) {
    NEW("new"),
    IN_PROGRESS("in_progress"),
    FINISHED("finished")
}

enum class OrderStatus(processSeq: Int, val status: String) {
    APPROVED(1, ""),
    CANCEL(1, ""),
    IN_WAY(2, "in-way"),
    ARRIVE(3, "arrive"),
    START_CHECK(4, "start-check"),
    CHECK(5, "check"),

    START_WORK(6, "start-work"),
    STOP_WORK(7, "stop-work"),

    FINISH_ORDER(8, "pay"),
    PAY_ORDER(9, "")
}
