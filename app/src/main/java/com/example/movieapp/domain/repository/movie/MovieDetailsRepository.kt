package com.example.movieapp.domain.repository.movie

import com.example.movieapp.domain.model.Credit
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Pagination

interface MovieDetailsRepository {

    suspend fun getMovieDetails(movieId: Int): Movie

    suspend fun getMovieCredits(movieId: Int): Credit

    suspend fun getSimilar(movieId: Int): Pagination<Movie>

}
