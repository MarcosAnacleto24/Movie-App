package com.example.movieapp.domain.repository.movie

import com.example.movieapp.domain.model.favorite.FavoriteMovie

interface FavoriteMovieRepository {

    suspend fun saveFavoriteMovie(movie: FavoriteMovie)

    suspend fun removeFavoriteMovie(movieId: Int)

    suspend fun getFavoriteMovies(): List<FavoriteMovie>

    suspend fun isFavoriteMovie(movieId: Int): Boolean

}