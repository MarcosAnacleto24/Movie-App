package com.example.movieapp.domain.repository.movie

import com.example.movieapp.domain.model.Credit
import com.example.movieapp.domain.model.Movie

interface MovieDetailsRepository {

    suspend fun getMovieDetails(movieId: Int): Movie

    suspend fun getMovieCredits(movieId: Int): Credit

}
