package com.example.movieapp.data.api

import com.example.movieapp.data.model.BasePaginationRemote
import com.example.movieapp.data.model.GenresResponse
import com.example.movieapp.data.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ServiceApi {

    @GET("genre/movie/list")
    suspend fun getGenres(
        @Query("language") language: String = "pt-br"
    ): GenresResponse


     @GET("discover/movie")
     suspend fun getMovieByGenre(
         @Query("with_genres") genreId: Int?,
         @Query("language") language: String = "pt-br"
     ): BasePaginationRemote<MovieResponse>





}