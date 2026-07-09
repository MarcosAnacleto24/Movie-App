package com.example.movieapp.presenter.main.moviegenre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.usecase.movie.GetMoviesByGenreUseCase
import com.example.movieapp.domain.usecase.movie.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

@HiltViewModel
class MovieGenreViewModel @Inject constructor(
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
): ViewModel() {

    // Retorna o Flow de PagingData diretamente, mantendo-o em cache no escopo do ViewModel
    fun getMoviesByGenre(genreId: Int): Flow<PagingData<Movie>> {
        return getMoviesByGenreUseCase(genreId)
            .cachedIn(viewModelScope)
    }

    fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return searchMoviesUseCase(query).cachedIn(viewModelScope)
    }
}