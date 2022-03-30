package com.app.namllahprovider.app

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.multidex.BuildConfig
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.exceptions.UndeliverableException
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

@HiltAndroidApp
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
        RxJavaPlugins.setErrorHandler { e ->
            if (e is UndeliverableException) {
                // Merely log undeliverable exceptions
                Log.v("ttt", e.message!!)
            } else {
                // Forward all others to current thread's uncaught exception handler
                Thread.currentThread().also { thread ->
                    thread.uncaughtExceptionHandler.uncaughtException(thread, e)
                }
            }
        }
        /*if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }*/
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    companion object {
        private const val TAG = "App"
    }
}