package com.app.namllahprovider.data.networkhelper

import com.app.namllahprovider.domain.Constants
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException
import java.io.InterruptedIOException
import java.net.HttpURLConnection
import java.net.URL

class NetworkConnectionInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val serverReachable = isConnected(Constants.BASE_URL)
        val googleReachable = isConnected("https://www.google.com/")
        if (!serverReachable && !googleReachable)
            throw NoConnectivityException("No Internet Connection!")
        else if (!serverReachable && googleReachable)
            throw NoConnectivityException("Server Connection Error!")
        val builder: Request.Builder = chain.request().newBuilder()
        return chain.proceed(builder.build())
    }

    private fun isConnected(urlStr: String): Boolean {
        try {
            val url = URL(urlStr)
            val urlc: HttpURLConnection = url.openConnection() as HttpURLConnection
            urlc.setRequestProperty("User-Agent", "Test")
            urlc.setRequestProperty("Connection", "close")
            urlc.connectTimeout = 1000 * 120 // mTimeout is in seconds
            urlc.connect()
            return urlc.responseCode == 200
        } catch (e1: InterruptedIOException) {
            return false
            throw e1
        } catch (e: IOException) {
            return false
            throw e
        }
    }
}