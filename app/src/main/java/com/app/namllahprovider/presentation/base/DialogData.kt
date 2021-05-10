package com.app.namllahprovider.presentation.base

import com.google.gson.Gson

class DialogData {
    var title: String = ""
    var message: String = ""

    constructor()

    constructor(title: String, message: String) : super() {
        this.title = title
        this.message = message
    }

    override fun toString(): String {
        return Gson().toJson(this)
    }
}