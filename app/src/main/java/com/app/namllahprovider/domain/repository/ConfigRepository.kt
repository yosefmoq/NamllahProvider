package com.app.namllahprovider.domain.repository

import com.app.namllahprovider.data.model.User


interface ConfigRepository {

    fun isLogin(): Boolean

    fun setLogin(isLogin: Boolean)

    fun isSeenOnBoarding(): Boolean

    fun setSeenOnBoarding(isSeenOnBoarding: Boolean)

    fun setLoggedUser(user: User)

    fun getLoggedUser(): User?

}