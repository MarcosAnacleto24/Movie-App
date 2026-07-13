package com.example.movieapp.presenter.main.bottombar.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.movieapp.domain.usecase.movie.SearchMoviesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
): ViewModel() {

    // Gatilho reativo da busca
    private val _searchQuery = MutableStateFlow("")


    @OptIn(ExperimentalCoroutinesApi::class)
    val searchMoviesFlow = _searchQuery
        .flatMapLatest { query ->
            if (query.isNotEmpty()) {
                searchMoviesUseCase(query).cachedIn(viewModelScope) // Guarda em cache no escopo
            } else {
                flowOf(PagingData.empty())
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily, // Mantém vivo em memória preservando o scroll
            initialValue = PagingData.empty()
        )

    // Atualiza o termo de busca disparando o flatMapLatest automaticamente
    fun searchMovies(query: String) {
        if (_searchQuery.value != query) {
            _searchQuery.value = query
        }
    }


}