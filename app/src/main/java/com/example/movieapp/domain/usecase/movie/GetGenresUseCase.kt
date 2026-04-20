package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.repository.movie.MovieRepository
import com.example.movieapp.presenter.model.GenrePresentation
import jakarta.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(): List<GenrePresentation> {
        return movieRepository.getGenres()
    }
}