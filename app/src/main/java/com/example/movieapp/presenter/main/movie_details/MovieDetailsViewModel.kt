package com.example.movieapp.presenter.main.movie_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.domain.usecase.movie.GetMovieDetailsUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase
): ViewModel()
{
    fun getMovieDetails(movieId: Int) = liveData(Dispatchers.IO)  {
        try {
            emit(StateView.Loading())

            val result = getMovieDetailsUseCase(movieId)


            emit(StateView.Success(result))


        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }
    }
}