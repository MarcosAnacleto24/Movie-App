package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.model.movie.MovieReview
import com.example.movieapp.domain.model.movie.Pagination
import com.example.movieapp.domain.repository.movie.MovieDetailsRepository
import javax.inject.Inject


class GetMovieReviewsUseCase @Inject constructor(
    private val movieDetailsRepository: MovieDetailsRepository
) {
    suspend operator fun invoke(movieId: Int): Pagination<MovieReview>  {
        return movieDetailsRepository.getReviews(movieId)
    }
}
