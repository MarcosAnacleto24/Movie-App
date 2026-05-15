package com.example.movieapp.presenter.main.bottombar.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.usecase.movie.SearchMoviesUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMoviesUseCase: SearchMoviesUseCase
): ViewModel() {

    private val _movieList = MutableLiveData<List<Movie>>()
    val movieList: LiveData<List<Movie>>
        get() = _movieList

    private val _searchState = MutableLiveData<StateView<Unit>>()
    val searchState: LiveData<StateView<Unit>>
        get() = _searchState

    fun searchMovies(query: String) {
       viewModelScope.launch {
           try {

               _searchState.postValue(StateView.Loading())

               val result = searchMoviesUseCase(query)

               // Enviamos apenas a lista de filmes
               _searchState.postValue(StateView.Success(Unit))
               _movieList.postValue(result.results?: emptyList())


           } catch (e: Exception) {
               e.printStackTrace()
               _searchState.postValue(StateView.Error(e.message))
           }
       }
    }
}