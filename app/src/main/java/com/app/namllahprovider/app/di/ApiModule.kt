package com.app.namllahprovider.app.di

import com.app.namllahprovider.data.api.auth.AuthApi
import com.app.namllahprovider.data.api.auth.AuthApiImpl
import com.app.namllahprovider.data.api.global.GlobalApi
import com.app.namllahprovider.data.api.global.GlobalApiImpl
import com.app.namllahprovider.data.api.notification.NotificationApi
import com.app.namllahprovider.data.api.notification.NotificationApiImpl
import com.app.namllahprovider.data.api.order.OrderApi
import com.app.namllahprovider.data.api.order.OrderApiImpl
import com.app.namllahprovider.data.api.user.UserApi
import com.app.namllahprovider.data.api.user.UserApiImpl
import com.app.namllahprovider.domain.repository.ConfigRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
class ApiModule {
    @Provides
    fun provideAuthApi(retrofit: Retrofit.Builder): AuthApi =
        retrofit.build().create(AuthApi::class.java)

    @Provides
    fun provideAuthApiImpl(authApi: AuthApi, configRepository: ConfigRepository) =
        AuthApiImpl(authApi, configRepository)

    @Provides
    fun provideOrderApi(retrofit: Retrofit.Builder): OrderApi =
        retrofit.build().create(OrderApi::class.java)

    @Provides
    fun provideOrderApiImpl(orderApi: OrderApi, configRepository: ConfigRepository) =
        OrderApiImpl(orderApi, configRepository)

    @Provides
    fun provideNotificationApi(retrofit: Retrofit.Builder): NotificationApi =
        retrofit.build().create(NotificationApi::class.java)

    @Provides
    fun provideNotificationApiImpl(
        notificationApi: NotificationApi,
        configRepository: ConfigRepository
    ) =
        NotificationApiImpl(notificationApi, configRepository)

    @Provides
    fun provideUserApi(retrofit: Retrofit.Builder): UserApi =
        retrofit.build().create(UserApi::class.java)

    @Provides
    fun provideUserApiImpl(userApi: UserApi, configRepository: ConfigRepository) =
        UserApiImpl(userApi, configRepository)

    @Provides
    fun provideGlobalApi(retrofit: Retrofit.Builder): GlobalApi =
        retrofit.build().create(GlobalApi::class.java)

    @Provides
    fun provideGlobalApiImpl(globalApi: GlobalApi, configRepository: ConfigRepository) =
        GlobalApiImpl(globalApi, configRepository)
}