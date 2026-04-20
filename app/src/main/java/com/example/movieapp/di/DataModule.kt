package com.example.movieapp.di

import com.example.movieapp.data.api.ServiceApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DataModule {
    @Provides
    @Singleton
    fun provideServiceAPI(retrofit: Retrofit): ServiceApi {
        return retrofit.create(ServiceApi::class.java)
    }
}