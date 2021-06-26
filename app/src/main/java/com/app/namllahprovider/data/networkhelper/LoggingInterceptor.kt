package com.app.namllahprovider.data.networkhelper

import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.data.sharedvariables.SharedVariables
import com.app.namllahprovider.domain.SharedValueFlags
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.ResponseBody
import timber.log.Timber
import java.util.*

class LoggingInterceptor(var sharedVariables: SharedVariables) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val token =
            sharedVariables.getStringSharedVariable(SharedValueFlags.USER_TOKEN) ?: ""
        val language =
            sharedVariables.getObjectFromSharedVariable<UserDto>(SharedValueFlags.USER)?.language?.code
                ?: "en"

        request = request.newBuilder()
            .header("Accept", "application/json")
            .header("Accept-Language", language)
            .header("Authorization", token)
            .method(request.method, request.body).build()

        Timber.tag(TAG).d("intercept: url ${request.url}")
        Timber.tag(TAG).d("intercept: method ${request.method}")
        Timber.tag(TAG).d("intercept: headers ${request.headers}")
        Timber.tag(TAG).d("intercept: body ${Gson().toJson(request.body)}")

        val t1 = System.nanoTime()

        val response = chain.proceed(request)
        val t2 = System.nanoTime()

        val responseLog = String.format(
            Locale.US, "Received response for %s in %.1fms%n%s",
            response.request.url, (t2 - t1) / 1e6, response.headers
        )

        val bodyString = response.body!!.string()
        Timber.tag(TAG).d("intercept : response\n$responseLog\n Response Body : $bodyString")


        return response.newBuilder()
            .body(ResponseBody.create(response.body!!.contentType(), bodyString))
            .build()
    }

    companion object {
        private const val TAG = "LoggingInterceptor"
    }
}