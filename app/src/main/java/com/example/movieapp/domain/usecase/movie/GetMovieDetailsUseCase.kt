package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.movie.MovieDetailsRepository
import javax.inject.Inject


class GetMovieDetailsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend operator fun invoke(movieId: Int): Movie  {
        return movieDetailsRepository.getMovieDetails(movieId)
    }
}
