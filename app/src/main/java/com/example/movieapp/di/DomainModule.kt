package com.example.movieapp.di

import com.example.movieapp.data.repository.auth.FirebaseAuthenticationImpl
import com.example.movieapp.data.repository.movie.MovieRepositoryImpl
import com.example.movieapp.domain.repository.auth.FirebaseAuthentication
import com.example.movieapp.domain.repository.movie.MovieRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    abstract fun bindsFirebaseAuthentication(
        firebaseAuthenticationImpl: FirebaseAuthenticationImpl
    ): FirebaseAuthentication

    @Binds
    abstract fun bindsMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

}