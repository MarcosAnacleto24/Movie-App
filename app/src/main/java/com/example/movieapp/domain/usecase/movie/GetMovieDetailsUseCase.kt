package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Pagination
import com.example.movieapp.domain.repository.movie.MovieRepository
import jakarta.inject.Inject

class GetMovieDetailsUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(movieId: Int): Movie  {
        return movieRepository.getMovieDetails(movieId)
    }
}
