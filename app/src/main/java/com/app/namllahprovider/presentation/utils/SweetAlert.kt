package com.app.namllahprovider.presentation.utils

import android.app.Activity
import android.content.Context
import android.view.Gravity
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.app.namllahprovider.R
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog
import com.tapadoo.alerter.Alerter
import org.json.JSONObject

class SweetAlert private constructor() {
    var sweetAlertDialog: SweetAlertDialog? = null

    companion object {
        var instance = SweetAlert()
    }

    fun showAlertDialog(
        context: Context,
        alertType: SweetAlertType,
        title: String,
        message: String,
        confirmText: String? = null,
        confirmListener: () -> Unit? = {},
        cancelText: String? = null,
        cancelListener: () -> Unit?  = {},
        cancelable: Boolean = false,
        @DrawableRes imageRes: Int = -1
    ) {
        sweetAlertDialog = SweetAlertDialog(context, alertType.code)
            .setTitleText(title)
            .setContentText(message)

        sweetAlertDialog?.setCancelable(cancelable)

        if (!confirmText.isNullOrEmpty()) {
            sweetAlertDialog?.confirmText = confirmText
            sweetAlertDialog?.setConfirmClickListener {
                it.dismissWithAnimation();
                confirmListener()
            }
        }

        if (!cancelText.isNullOrEmpty()) {
            sweetAlertDialog?.showCancelButton(true)
            sweetAlertDialog?.cancelText = cancelText
            sweetAlertDialog?.setCancelClickListener {
                it.cancel()
                cancelListener()
            }
        } else {
            sweetAlertDialog?.showCancelButton(false)
        }

        if (imageRes != -1) {
            sweetAlertDialog?.setCustomImage(imageRes)
        }

        sweetAlertDialog?.show()
    }

    fun dismissAlertDialog(dismissWithAnimation: Boolean = true) {
        if (sweetAlertDialog?.isShowing == true) {
            if (dismissWithAnimation)
                sweetAlertDialog?.dismissWithAnimation()
            else
                sweetAlertDialog?.dismiss()
        }
    }

    fun showFailAlert(
        activity: Activity,
        message: String,
        isJson: Boolean = false,
    ) {
        val finalMessage = if (isJson) {
            try {
                val jsonObject = JSONObject(message)
                val errors = jsonObject.getJSONObject("errors")
                val isMobileError = errors.has("mobile")
                val isPasswordError = errors.has("password")
                val isStatusError = errors.has("status")
                when {
                    isMobileError -> errors.getJSONArray("mobile").getString(0)
                    isPasswordError -> errors.getJSONArray("password").getString(0)
                    isStatusError -> errors.getJSONArray("status").getString(0)
                    else -> "Something other error, Please try again later."
                }
            } catch (e: Exception) {
                message
            }
        } else {
            message
        }
        Alerter.create(activity)
            .setText(finalMessage)
            .setTitleAppearance(R.style.AlertTextAppearance_Title)
            .setTextAppearance(R.style.AlertTextAppearance_Text)
            .setBackgroundColorInt(ContextCompat.getColor(activity, R.color.red))
            .enableInfiniteDuration(false)
            .setDuration(3000)
            .setContentGravity(Gravity.END)
            .show()
    }

    fun showFailAlert(activity: Activity, throwable: Throwable) {
        val message = throwable.message ?: ""
        Alerter.create(activity)
            .setText(message)
            .setTitleAppearance(R.style.AlertTextAppearance_Title)
            .setTextAppearance(R.style.AlertTextAppearance_Text)
            .setBackgroundColorInt(ContextCompat.getColor(activity, R.color.red))
            .enableInfiniteDuration(false)
            .setDuration(3000)
            .setContentGravity(Gravity.END)
            .show()
    }


    fun showSuccessAlert(activity: Activity, message: String) {
        Alerter.create(activity)
            .setTitle(message)
            .setBackgroundColorInt(ContextCompat.getColor(activity, R.color.green_light))
            .enableInfiniteDuration(false)
            .setDuration(3000)
            .setTitleAppearance(R.style.AlertTextAppearance_Title)
            .setTextAppearance(R.style.AlertTextAppearance_Text)
            .setContentGravity(Gravity.END)
            .show()
    }
}