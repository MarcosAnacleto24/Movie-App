package com.example.movieapp.presenter.main.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.domain.usecase.movie.GetMovieSimilarUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class SimilarViewModel @Inject constructor (
    private val getMovieSimilarUseCase: GetMovieSimilarUseCase
): ViewModel() {

    fun getMovieSimilar(movieId: Int) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val response = getMovieSimilarUseCase(movieId)
            emit(StateView.Success(response))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }

    }
}