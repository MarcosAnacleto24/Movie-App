package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Pagination
import com.example.movieapp.domain.repository.movie.MovieDetailsRepository
import jakarta.inject.Inject

class GetMovieSimilarUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend operator fun invoke(movieId: Int): Pagination<Movie>  {
        return movieDetailsRepository.getSimilar(movieId)
    }
}
