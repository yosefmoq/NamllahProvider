package com.app.namllahprovider.domain.repository

import com.app.namllahprovider.data.model.UserDto


interface ConfigRepository {

    fun isLogin(): Boolean

    fun setLogin(isLogin: Boolean)

    fun isSeenOnBoarding(): Boolean

    fun setSeenOnBoarding(isSeenOnBoarding: Boolean)

    fun setLoggedUser(userDto: UserDto?)

    fun getLoggedUser(): UserDto?

    fun setLanguage(languageCode: String)

    fun getLanguage(): String

    fun setUserToken(userToken: String)

    fun getUserToken(): String

    fun clearConfigData()

    fun setFCMToken(fcmToken: String)

    fun getFCMToken(): String

    fun setEstTime(estTime:Double)

    fun getEstTime():Double

}