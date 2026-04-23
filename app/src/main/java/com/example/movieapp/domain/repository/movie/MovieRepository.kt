package com.example.movieapp.domain.repository.movie

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Pagination
import com.example.movieapp.presenter.model.GenrePresentation

interface MovieRepository {

    suspend fun getGenres(): List<GenrePresentation>

    suspend fun getMoviesByGenre(genreId: Int): Pagination<Movie>

    suspend fun searchMovies(query: String): Pagination<Movie>

    suspend fun getMovieDetails(movieId: Int): Movie

}
