package com.example.movieapp.di

import com.example.movieapp.data.local.repository.MovieLocalRepositoryImpl
import com.example.movieapp.data.repository.auth.FirebaseAuthenticationImpl
import com.example.movieapp.data.repository.movie.FavoriteMovieRepositoryImpl
import com.example.movieapp.data.repository.movie.MovieDetailsRepositoryImpl
import com.example.movieapp.data.repository.movie.MovieRepositoryImpl
import com.example.movieapp.data.repository.user.UserRepositoryImpl
import com.example.movieapp.domain.local.repository.MovieLocalRepository
import com.example.movieapp.domain.repository.auth.FirebaseAuthentication
import com.example.movieapp.domain.repository.movie.FavoriteMovieRepository
import com.example.movieapp.domain.repository.movie.MovieDetailsRepository
import com.example.movieapp.domain.repository.movie.MovieRepository
import com.example.movieapp.domain.repository.user.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun bindsFirebaseAuthentication(
        firebaseAuthenticationImpl: FirebaseAuthenticationImpl
    ): FirebaseAuthentication

    @Binds
    @Singleton
    abstract fun bindsMovieRepository(
        movieRepositoryImpl: MovieRepositoryImpl
    ): MovieRepository

    @Binds
    @Singleton
    abstract fun bindsMovieDetailsRepository(
        movieDetailsRepositoryImpl: MovieDetailsRepositoryImpl
    ): MovieDetailsRepository

    @Binds
    @Singleton
    abstract fun bindsMovieLocalRepository(
        movieLocalRepositoryImpl: MovieLocalRepositoryImpl
    ): MovieLocalRepository

    @Binds
    @Singleton
    abstract fun bindsUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindsFavoriteMovieRepository(
        favoriteMovieRepositoryImpl: FavoriteMovieRepositoryImpl
    ): FavoriteMovieRepository

}
