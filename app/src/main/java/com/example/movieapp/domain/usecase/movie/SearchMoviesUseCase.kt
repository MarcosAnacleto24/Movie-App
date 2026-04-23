package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Pagination
import com.example.movieapp.domain.repository.movie.MovieRepository
import jakarta.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(query: String): Pagination<Movie> {
        return movieRepository.searchMovies(query)
    }
}
