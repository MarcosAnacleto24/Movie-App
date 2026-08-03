package com.example.movieapp.domain.usecase.favorite

import com.example.movieapp.domain.model.favorite.FavoriteMovie
import com.example.movieapp.domain.repository.movie.FavoriteMovieRepository
import javax.inject.Inject

class GetFavoriteMoviesUseCase @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository
) {
    suspend operator fun invoke(): List<FavoriteMovie> {
       return favoriteMovieRepository.getFavoriteMovies()
    }
}