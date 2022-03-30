package com.app.namllahprovider.data.repository

import android.content.Context
import com.app.namllahprovider.data.model.UserDto
import com.app.namllahprovider.data.sharedvariables.SharedVariables
import com.app.namllahprovider.domain.Constants
import com.app.namllahprovider.domain.SharedValueFlags
import com.app.namllahprovider.domain.repository.ConfigRepository

import javax.inject.Inject


class ConfigRepositoryImpl @Inject constructor(
    private val context: Context
) : ConfigRepository {

    var sharedVariables: SharedVariables = SharedVariables(context)

    override fun isLogin(): Boolean = ConfigRepositoryObj.isLogin(context)

    override fun setLogin(isLogin: Boolean) {
        sharedVariables.setBooleanSharedVariable(SharedValueFlags.IS_LOGIN, isLogin)
    }

    override fun isSeenOnBoarding(): Boolean = ConfigRepositoryObj.isSeenOnBoarding(context)

    override fun setSeenOnBoarding(isSeenOnBoarding: Boolean) {
        sharedVariables.setBooleanSharedVariable(
            SharedValueFlags.IS_SEEN_ON_BOARDING,
            isSeenOnBoarding
        )
    }

    override fun setLoggedUser(userDto: UserDto?) {
        sharedVariables.setObjectInSharedVariable(
            SharedValueFlags.USER,
            userDto
        )
    }

    override fun getLoggedUser(): UserDto? = ConfigRepositoryObj.getLoggedUser(context)

    override fun setLanguage(languageCode: String) {
        sharedVariables.setStringSharedVariable(
            SharedValueFlags.LANGUAGE,
            languageCode
        )
    }

    override fun getLanguage(): String = ConfigRepositoryObj.getLanguage(context)

    override fun setUserToken(userToken: String) {
        sharedVariables.setStringSharedVariable(
            SharedValueFlags.USER_TOKEN,
            Constants.BEARER_TOKEN + userToken
        )
    }

    override fun getUserToken(): String =  ConfigRepositoryObj.getUserToken(context)

    override fun setFCMToken(fcmToken: String) {
        sharedVariables.setStringSharedVariable(
            SharedValueFlags.FCM_TOKEN,
            fcmToken
        )
    }

    override fun getFCMToken(): String =  ConfigRepositoryObj.getFCMToken(context)
    override fun setEstTime(estTime: Double) {
        sharedVariables.setDoubleSharedVariable(
            SharedValueFlags.EST_CHECK,
            estTime
        )
    }

    override fun getEstTime(): Double  = ConfigRepositoryObj.getEstTime(context)

    override fun clearConfigData() {
        sharedVariables.clearConfigData()
    }
}