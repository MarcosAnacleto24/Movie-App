package com.example.movieapp.data.api

import com.example.movieapp.data.model.movie.BasePaginationRemote
import com.example.movieapp.data.model.movie.CreditResponse
import com.example.movieapp.data.model.movie.GenresResponse
import com.example.movieapp.data.model.movie.MovieResponse
import com.example.movieapp.data.model.movie.MovieReviewResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ServiceApi {

    @GET("genre/movie/list")
    suspend fun getGenres(): GenresResponse

    @GET("discover/movie")
    suspend fun getMovieByGenre(
     @Query("with_genres") genreId: Int?,
     @Query("page") page: Int = 1
    ): BasePaginationRemote<MovieResponse>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("query") query: String?,
        @Query("page") page: Int = 1
    ): BasePaginationRemote<MovieResponse>

    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int?,
    ): MovieResponse

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int?,
    ): CreditResponse

    @GET("movie/{movie_id}/similar")
    suspend fun getSimilar(
        @Path("movie_id") movieId: Int?,
    ):  BasePaginationRemote<MovieResponse>


    @GET("movie/{movie_id}/reviews")
    suspend fun getReviews(
        @Path("movie_id") movieId: Int?,
    ): BasePaginationRemote<MovieReviewResponse>
}