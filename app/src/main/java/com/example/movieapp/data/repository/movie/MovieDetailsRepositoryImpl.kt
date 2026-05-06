package com.example.movieapp.data.repository.movie

import com.example.movieapp.data.api.ServiceApi
import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.model.Credit
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Pagination
import com.example.movieapp.domain.repository.movie.MovieDetailsRepository
import jakarta.inject.Inject

class MovieDetailsRepositoryImpl @Inject constructor(
    private val serviceApi: ServiceApi
): MovieDetailsRepository {

    override suspend fun getMovieDetails(movieId: Int): Movie {
        val response = serviceApi.getMovieDetails(movieId)

        return response.toDomain()
    }

    override suspend fun getMovieCredits(movieId: Int): Credit {
        val response = serviceApi.getMovieCredits(movieId)

        return response.toDomain()
    }

    override suspend fun getSimilar(movieId: Int): Pagination<Movie> {
        val response = serviceApi.getSimilar(movieId)

        return response.toDomain()
    }

}