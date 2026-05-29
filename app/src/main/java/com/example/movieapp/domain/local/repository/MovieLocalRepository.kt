package com.example.movieapp.domain.local.repository

import com.example.movieapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MovieLocalRepository {

    fun getMovies(): Flow<List<Movie>>

    suspend fun insertMovie(movie: Movie)

    suspend fun deleteMovie(movieId: Int)
}