package com.example.movieapp.domain.usecase.favorite

import com.example.movieapp.domain.repository.movie.FavoriteMovieRepository
import javax.inject.Inject

class RemoveFavoriteMovieUseCase @Inject constructor(
    private val favoriteMovieRepository: FavoriteMovieRepository
) {
    suspend operator fun invoke(movieId: Int) {
        favoriteMovieRepository.removeFavoriteMovie(movieId)
    }
}