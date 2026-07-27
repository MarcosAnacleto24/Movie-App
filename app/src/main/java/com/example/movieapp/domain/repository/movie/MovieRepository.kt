package com.example.movieapp.domain.repository.movie

import androidx.paging.PagingData
import com.example.movieapp.domain.model.movie.Movie
import com.example.movieapp.domain.model.movie.Pagination
import com.example.movieapp.presenter.model.GenrePresentation
import kotlinx.coroutines.flow.Flow

interface MovieRepository {

    suspend fun getGenres(): List<GenrePresentation>

    fun getMoviesByGenre(genreId: Int): Flow<PagingData<Movie>>

    fun searchMovies(query: String): Flow<PagingData<Movie>>

    suspend fun getMoviesByGenreList(genreId: Int): Pagination<Movie>

}
