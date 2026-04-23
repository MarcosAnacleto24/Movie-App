package com.example.movieapp.presenter.main.moviegenre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.domain.usecase.movie.GetMoviesByGenreUseCase
import com.example.movieapp.domain.usecase.movie.SearchMoviesUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class MovieGenreViewModel @Inject constructor(
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
): ViewModel() {

    fun getMoviesByGenre(genreId: Int) = liveData(Dispatchers.IO)  {
        try {
            emit(StateView.Loading())

            val result = getMoviesByGenreUseCase(genreId)

            // Enviamos apenas a lista de filmes
            emit(StateView.Success(result.results))


        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }
    }

    fun searchMovies(query: String) = liveData(Dispatchers.IO)  {
        try {
            emit(StateView.Loading())

            val result = searchMoviesUseCase(query)

            // Enviamos apenas a lista de filmes
            emit(StateView.Success(result.results))


        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }
    }
}