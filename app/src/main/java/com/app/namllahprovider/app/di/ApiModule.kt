package com.app.namllahprovider.app.di

import com.app.namllahprovider.data.api.auth.AuthApi
import com.app.namllahprovider.data.api.auth.AuthApiImpl
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
}