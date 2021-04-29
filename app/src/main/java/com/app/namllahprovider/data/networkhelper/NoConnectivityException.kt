package com.app.namllahprovider.data.networkhelper

import java.io.InterruptedIOException

class NoConnectivityException(message: String) : InterruptedIOException(message) {

    override val message: String?
        get() = super.message
}