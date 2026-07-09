package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.model.Credit
import com.example.movieapp.domain.repository.movie.MovieDetailsRepository
import javax.inject.Inject


class GetMovieCreditsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend operator fun invoke(movieId: Int): Credit  {
        return movieDetailsRepository.getMovieCredits(movieId)
    }
}
