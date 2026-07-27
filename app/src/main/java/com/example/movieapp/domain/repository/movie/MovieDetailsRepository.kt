package com.example.movieapp.domain.repository.movie

import com.example.movieapp.domain.model.movie.Credit
import com.example.movieapp.domain.model.movie.Movie
import com.example.movieapp.domain.model.movie.MovieReview
import com.example.movieapp.domain.model.movie.Pagination

interface MovieDetailsRepository {

    suspend fun getMovieDetails(movieId: Int): Movie

    suspend fun getMovieCredits(movieId: Int): Credit

    suspend fun getSimilar(movieId: Int): Pagination<Movie>

    suspend fun getReviews(movieId: Int): Pagination<MovieReview>

}
