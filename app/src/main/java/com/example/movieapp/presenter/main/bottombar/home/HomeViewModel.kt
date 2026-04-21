package com.example.movieapp.presenter.main.bottombar.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.domain.usecase.movie.GetGenresUseCase
import com.example.movieapp.domain.usecase.movie.GetMoviesByGenreUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase
): ViewModel() {

    fun getGenres() = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            // Busca a lista de gêneros (GenrePresentation)
            val genres = getGenresUseCase()

            // Para cada gênero, busca os filmes correspondentes
            genres.forEach { genre ->
                genre.id?.let { id ->
                    // Busca a paginação e extrai a lista de filmes (List<Movie>)
                    val pagination = getMoviesByGenreUseCase(id)
                    genre.movies = pagination.results?.take(5)
                }
            }

            // Emite o sucesso com a lista de gêneros já preenchida com filmes
            emit(StateView.Success(genres))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }
    }
}