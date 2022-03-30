package com.app.namllahprovider.data.sharedvariables

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.app.namllahprovider.domain.Constants
import com.app.namllahprovider.domain.SharedValueFlags
import com.google.gson.Gson
import java.util.concurrent.Semaphore
import javax.inject.Inject


class SharedVariables @Inject constructor(mContext: Context) {

    var mSharedPreference: SharedPreferences? = null

    init {
        mSharedPreference = PreferenceManager.getDefaultSharedPreferences(mContext)
    }

    val mSharedPreferenceEditor: SharedPreferences.Editor

    val mSemaphore: Semaphore

    fun setStringSharedVariable(flag: SharedValueFlags?, value: String?) {
        try {
            mSemaphore.acquire()
            mSharedPreferenceEditor.putString(java.lang.String.valueOf(flag), value)
            mSharedPreferenceEditor.commit()
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun getStringSharedVariable(flag: SharedValueFlags): String? {
        var returnValue: String? = SPF_STRING_NO_VALUE_FOUND
        try {
            mSemaphore.acquire()
            returnValue = mSharedPreference!!.getString(
                java.lang.String.valueOf(flag),
                SPF_STRING_NO_VALUE_FOUND
            )
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return returnValue
    }

    fun setStringListSharedVariable(flag: SharedValueFlags, value: List<String>) {
        try {
            mSemaphore.acquire()
            mSharedPreferenceEditor.putStringSet(java.lang.String.valueOf(flag), value.toSet())
            mSharedPreferenceEditor.commit()
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun getStringListSharedVariable(flag: SharedValueFlags): List<String>? {
        var returnValue: List<String>? = SPF_STRING_LIST_NO_VALUE_FOUND
        try {
            mSemaphore.acquire()
            returnValue = mSharedPreference!!.getStringSet(
                java.lang.String.valueOf(flag),
                SPF_STRING_LIST_NO_VALUE_FOUND.toSet()
            )?.toList()
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return returnValue
    }

    fun setIntSharedVariable(flag: SharedValueFlags, value: Int) {
        try {
            mSemaphore.acquire()
            mSharedPreferenceEditor.putInt(java.lang.String.valueOf(flag), value)
            mSharedPreferenceEditor.commit()
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun getIntSharedVariable(flag: SharedValueFlags): Int {
        var returnValue = SPF_INT_NO_VALUE_FOUND
        try {
            mSemaphore.acquire()
            returnValue = mSharedPreference!!.getInt(
                java.lang.String.valueOf(flag),
                SPF_INT_NO_VALUE_FOUND
            )
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return returnValue
    }

    fun getLongSharedVariable(flag: SharedValueFlags): Long {
        var returnValue = SPF_LONG_NO_VALUE_FOUND
        try {
            mSemaphore.acquire()
            returnValue = mSharedPreference!!.getLong(
                java.lang.String.valueOf(flag),
                SPF_LONG_NO_VALUE_FOUND
            )
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return returnValue
    }

    fun setLongSharedVariable(flag: SharedValueFlags, value: Long) {
        try {
            mSemaphore.acquire()
            mSharedPreferenceEditor.putLong(java.lang.String.valueOf(flag), value)
            mSharedPreferenceEditor.commit()
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun getDoubleSharedVariable(flag: SharedValueFlags): Double {
        var returnValue: Float = SPF_FLOAT_NO_VALUE_FOUND.toFloat()
        try {
            mSemaphore.acquire()
            returnValue = mSharedPreference!!.getFloat(
                java.lang.String.valueOf(flag),
                SPF_FLOAT_NO_VALUE_FOUND.toFloat()
            )
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return returnValue.toDouble()
    }

    fun setDoubleSharedVariable(flag: SharedValueFlags, value: Double) {
        try {
            mSemaphore.acquire()
            mSharedPreferenceEditor.putFloat(java.lang.String.valueOf(flag), value.toFloat())
            mSharedPreferenceEditor.commit()
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun setBooleanSharedVariable(flag: SharedValueFlags, value: Boolean) {
        try {
            mSemaphore.acquire()
            mSharedPreferenceEditor.putBoolean(java.lang.String.valueOf(flag), value)
            mSharedPreferenceEditor.commit()
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    fun getBooleanSharedVariable(flag: SharedValueFlags): Boolean {
        var returnValue = SPF_BOOLEAN_NO_VALUE_FOUND
        try {
            mSemaphore.acquire()
            returnValue = mSharedPreference!!.getBoolean(
                java.lang.String.valueOf(flag),
                SPF_BOOLEAN_NO_VALUE_FOUND
            )
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return returnValue
    }


    fun setObjectInSharedVariable(flag: SharedValueFlags, value: Any?) {
        try {
            val gson = Gson()
            val stringValue = gson.toJson(value)
            mSemaphore.acquire()
            mSharedPreferenceEditor.putString(java.lang.String.valueOf(flag), stringValue)
            mSharedPreferenceEditor.commit()
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    inline fun <reified T> getObjectFromSharedVariable(flag: SharedValueFlags): T? {
        var returnValue: T? = null
        try {
            val gson = Gson()
            mSemaphore.acquire()
            val stringValue = mSharedPreference!!.getString(
                java.lang.String.valueOf(flag),
                SPF_STRING_NO_VALUE_FOUND
            )
            returnValue = gson.fromJson(stringValue, T::class.java)
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        return returnValue
    }

    fun clearConfigData(){
        try {
            mSemaphore.acquire()
            mSharedPreferenceEditor.clear()
            mSharedPreferenceEditor.commit()
            mSemaphore.release()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
    }

    companion object DefaultSVValue {
        const val SPF_STRING_NO_VALUE_FOUND = "NULL"
        val SPF_STRING_LIST_NO_VALUE_FOUND = mutableListOf<String>()

        const val SPF_INT_NO_VALUE_FOUND = -1
        const val SPF_LONG_NO_VALUE_FOUND = -1L
        const val SPF_FLOAT_NO_VALUE_FOUND = -1.0
        const val SPF_BOOLEAN_NO_VALUE_FOUND = false
    }

    init {
        @SuppressLint("CommitPrefEdits")
        mSharedPreferenceEditor = mSharedPreference!!.edit()
        mSemaphore = Semaphore(
            Constants.MAX_PROCESS_AVAILABLE,
            true
        )
    }
}
