package com.example.movieapp.presenter.main.bottombar.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.favorite.FavoriteMovie
import com.example.movieapp.domain.usecase.favorite.GetFavoriteMoviesUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase
) : ViewModel() {

    private val _favoriteList = MutableLiveData<StateView<List<FavoriteMovie>>>()
    val favoriteList: LiveData<StateView<List<FavoriteMovie>>> = _favoriteList

    private val _favoriteSearchList = MutableLiveData<List<FavoriteMovie>>(emptyList())
    val favoriteSearchList: LiveData<List<FavoriteMovie>> = _favoriteSearchList

    fun getFavoriteMovies() = viewModelScope.launch {
        _favoriteList.postValue(StateView.Loading())
        try {
            val movies = getFavoriteMoviesUseCase()
            _favoriteList.postValue(StateView.Success(movies))
        } catch (e: Exception) {
            _favoriteList.postValue(StateView.Error(e.message))
        }
    }

    fun searchMovie(query: String) {
        val currentMovies = (favoriteList.value as? StateView.Success)?.data ?: emptyList()
        if (query.isBlank()) {
            _favoriteSearchList.postValue(emptyList())
        } else {
            val filtered = currentMovies.filter {
                it.title?.contains(query, ignoreCase = true) == true
            }
            _favoriteSearchList.postValue(filtered)
        }
    }
}