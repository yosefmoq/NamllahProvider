package com.app.namllahprovider.data.repository

import android.content.Context
import com.app.namllahprovider.data.sharedvariables.SharedVariables
import com.app.namllahprovider.domain.SharedValueFlags


object ConfigRepositoryObj {

    fun isLogin(context: Context): Boolean =
        SharedVariables(context).getBooleanSharedVariable(SharedValueFlags.IS_LOGIN)

    fun isSeenOnBoarding(context: Context): Boolean =
        SharedVariables(context).getBooleanSharedVariable(SharedValueFlags.IS_SEEN_ON_BOARDING)
}