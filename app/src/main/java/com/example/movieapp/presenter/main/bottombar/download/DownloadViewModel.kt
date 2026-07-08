package com.example.movieapp.presenter.main.bottombar.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.local.usecase.DeleteMoviesUseCase
import com.example.movieapp.domain.local.usecase.GetMoviesUseCase
import com.example.movieapp.domain.model.Movie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DownloadViewModel @Inject constructor(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val deleteMoviesUseCase: DeleteMoviesUseCase

): ViewModel() {

    private val _movieList = MutableLiveData(mutableListOf<Movie>())
    var movieList: LiveData<MutableList<Movie>> = _movieList

    private val _movieSearchList = MutableLiveData(mutableListOf<Movie>())
    var movieSearchList: LiveData<MutableList<Movie>> = _movieSearchList

    init {
        getMovies()
    }

    private  fun getMovies() = viewModelScope.launch {
        getMoviesUseCase().collect { movies ->
            _movieList.postValue(movies.toMutableList())
        }
    }

    fun deleteMovies(movieId: Int) = viewModelScope.launch {
        deleteMoviesUseCase(movieId)
        getMovies()
    }

    fun searchMovie(search: String) = viewModelScope.launch {
        if (search.isBlank()) {
            _movieSearchList.postValue(mutableListOf()) // Limpa se o texto for vazio
        } else {
        val newList = _movieList.value?.filter { it.title?.contains(search, ignoreCase = true) == true }
        _movieSearchList.postValue(newList?.toMutableList())
        }
    }
}