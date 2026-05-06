package com.example.movieapp.presenter.main.movie_details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.domain.usecase.movie.GetMovieCreditsUseCase
import com.example.movieapp.domain.usecase.movie.GetMovieDetailsUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieCreditsUseCase: GetMovieCreditsUseCase
): ViewModel()
{
    private val _movieId = MutableLiveData<Int>()
    val movieId: MutableLiveData<Int> = _movieId
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

    fun getMovieCredits(movieId: Int) = liveData(Dispatchers.IO)  {
        try {
            emit(StateView.Loading())

            val result = getMovieCreditsUseCase(movieId)


            emit(StateView.Success(result))


        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }
    }

    fun setMovieId(movieId: Int) {
        _movieId.value = movieId
    }
}