package com.example.movieapp.presenter.main.bottombar.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.usecase.movie.GetGenresUseCase
import com.example.movieapp.domain.usecase.movie.GetMoviesByGenreListUseCase
import com.example.movieapp.presenter.model.GenrePresentation
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
    private val getMoviesByGenreListUseCase: GetMoviesByGenreListUseCase
): ViewModel() {

    private val _genresState = MutableStateFlow<StateView<List<GenrePresentation>>>(StateView.Loading())
    val genresState: StateFlow<StateView<List<GenrePresentation>>> = _genresState.asStateFlow()


    fun fetchGenresIfNeeded() {
        // Se o estado atual já for de sucesso, ignora o gatilho e preserva os dados na memória!
        if (_genresState.value is StateView.Success) return

        viewModelScope.launch {
            try {
                _genresState.value = StateView.Loading()

                val genresWithMovies = withContext(Dispatchers.IO) {
                    val genres = getGenresUseCase()

                    genres.forEach { genre ->
                        genre.id?.let { id ->
                            val pagination = getMoviesByGenreListUseCase(id)
                            genre.movies = pagination.results?.take(5)
                        }
                    }
                    genres
                }

                // Atualiza o StateFlow com o sucesso
                _genresState.value = StateView.Success(genresWithMovies)

            } catch (e: Exception) {
                e.printStackTrace()
                _genresState.value = StateView.Error(e.message)
            }
        }
    }

}

