package com.app.namllahprovider.app.di

import com.app.namllahprovider.data.repository.*
import com.app.namllahprovider.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun authRepository(authRepositoryImpl: AuthRepositoryImpl): AuthRepository

    @Binds
    abstract fun orderRepository(orderRepositoryImpl: OrderRepositoryImpl): OrderRepository

    @Binds
    abstract fun notificationRepository(notificationRepositoryImpl: NotificationRepositoryImpl):NotificationRepository

    @Binds
    abstract fun userRepository(notificationRepositoryImpl: UserRepositoryImpl):UserRepository

    @Binds
    abstract fun globalRepository(globalRepositoryImpl: GlobalRepositoryImpl):GlobalRepository

}