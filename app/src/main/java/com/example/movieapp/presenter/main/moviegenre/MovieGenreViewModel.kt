package com.example.movieapp.presenter.main.moviegenre

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.movieapp.domain.usecase.movie.GetMoviesByGenreUseCase
import com.example.movieapp.domain.usecase.movie.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class MovieGenreViewModel @Inject constructor(
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase,
    private val searchMoviesUseCase: SearchMoviesUseCase
): ViewModel() {

    // Guarda apenas o ID do gênero ativo
    private val _currentGenreId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val moviesByGenreFlow = _currentGenreId
        .flatMapLatest { genreId ->
            if (genreId != null) {
                // Dispara o usecase e faz o cache do Paging atrelado ao escopo da ViewModel
                getMoviesByGenreUseCase(genreId).cachedIn(viewModelScope)
            } else {
                flowOf(PagingData.empty())
            }
        }
        // Mantem o estado vivo ao navegar pelas telas!
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily, // Segura os dados e a posição do scroll na ViewModel
            initialValue = PagingData.empty()
        )

    // O Fragment chama essa função no onViewCreated para atualizar o gatilho se o ID mudar
    fun setGenreId(genreId: Int) {
        if (_currentGenreId.value != genreId) {
            _currentGenreId.value = genreId
        }
    }


    // BUSCA INTERNA (Fluxo Quente Estático para guardar a posição do scroll)
    private val _searchQuery = MutableStateFlow("")

    @OptIn(ExperimentalCoroutinesApi::class)
    val searchMoviesFlow = combine(_searchQuery, _currentGenreId) { query, genreId ->
        Pair(query, genreId)
    }
    .flatMapLatest { (query, genreId) ->
        if (query.isNotEmpty()) {
            searchMoviesUseCase(query)
                .cachedIn(viewModelScope)
                .map { pagingData ->
                    if (genreId != null) {
                        pagingData.filter { movie ->
                            movie.genreIds?.contains(genreId) == true
                        }
                    } else {
                        pagingData
                    }
                }
        } else {
            flowOf(PagingData.empty())
        }
    }
    .stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily, // Segura os dados e a posição do scroll na ViewModel
        initialValue = PagingData.empty()
    )

    // O fragmento chama essa função para atualizar o termo de busca
    fun searchMovies(query: String) {
        if (_searchQuery.value != query) {
            _searchQuery.value = query
        }
    }
}