package com.app.namllahprovider.domain.repository


interface ConfigRepository {

    fun isLogin():Boolean

    fun setLogin(isLogin: Boolean)

    fun isSeenOnBoarding():Boolean

    fun setSeenOnBoarding(isSeenOnBoarding: Boolean)

}