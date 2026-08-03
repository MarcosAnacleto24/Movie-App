package com.example.movieapp.domain.usecase.favorite

import com.example.movieapp.domain.repository.movie.FavoriteMovieRepository
import javax.inject.Inject

class IsFavoriteMovieUseCase @Inject constructor(
    private val repository: FavoriteMovieRepository
) {
    suspend operator fun invoke(movieId: Int): Boolean {
        return repository.isFavoriteMovie(movieId)
    }
}