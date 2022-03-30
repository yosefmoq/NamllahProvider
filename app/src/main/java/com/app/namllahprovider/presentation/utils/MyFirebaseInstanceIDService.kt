package com.app.namllahprovider.presentation.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.app.namllahprovider.R
import com.app.namllahprovider.domain.Constants
import com.app.namllahprovider.presentation.MainActivity

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import io.reactivex.subjects.PublishSubject
import timber.log.Timber

class MyFirebaseInstanceIDService : FirebaseMessagingService() {
    companion object{
        private const val TAG = "MyFirebaseInstanceIDSer"

        private var tokenData: PublishSubject<String> = PublishSubject.create()

        fun getObservable(): PublishSubject<String> {
            return tokenData
        }
    }

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Timber.tag(TAG).d(p0)
        tokenData.onNext(p0)
    }
    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        Timber.tag(TAG).d("new Message")


        // Check if message contains a data payload.
        if (p0.data.isNotEmpty()) {
            Timber.tag(TAG).d("Message data payload: ${ p0.data}")
            if(p0.data["type"] == Constants.NEW_ORDER){
                Notification.instance!!.addOrder("")
            }
            if ( /* Check if data needs to be processed by long running job */true) {
                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
            } else {
                // Handle message within 10 seconds
                handleNow()
            }
        }

        // Check if message contains a notification payload.

        // Check if message contains a notification payload.
        if (p0.notification != null) {
            Timber.tag(TAG).d("Message Notification Body: %s", p0.notification!!.body)
            tokenData.onNext(p0.notification!!.body!!)
            sendNotification(
                p0.notification!!.title!!,
                p0.notification!!.body!!
            )
        }

    }

    private fun sendNotification(
        messageTitle: String,
        messageBody: String
    ) {
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_NO_CREATE
        )
        val channelId = getString(R.string.default_notification_channel_id)
        val notificationBuilder =
            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_blue_logo)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setContentIntent(pendingIntent)
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
    private fun handleNow() {
        Log.d("ttt", "Short lived task is done.")
    }


    class Notification private constructor() {
        private val newOrder: MutableLiveData<String> = MutableLiveData()
        fun getNewOrder(): LiveData<String> {
            return newOrder
        }

        fun addOrder(orderID: String) {
            newOrder.postValue(orderID)
        }

        companion object {
            var instance: Notification? = null
                get() {
                    if (field == null) {
                        field = Notification()
                    }
                    return field
                }
                private set
        }

    }
}