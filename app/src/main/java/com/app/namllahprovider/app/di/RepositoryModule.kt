package com.app.namllahprovider.app.di

import com.app.namllahprovider.data.repository.AuthRepositoryImpl
import com.app.namllahprovider.data.repository.NotificationRepositoryImpl
import com.app.namllahprovider.data.repository.OrderRepositoryImpl
import com.app.namllahprovider.data.repository.UserRepositoryImpl
import com.app.namllahprovider.domain.repository.AuthRepository
import com.app.namllahprovider.domain.repository.NotificationRepository
import com.app.namllahprovider.domain.repository.OrderRepository
import com.app.namllahprovider.domain.repository.UserRepository
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

}