package com.example.movieapp.presenter.main.moviedetails.comments

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.domain.usecase.movie.GetMovieReviewsUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class CommentsViewModel @Inject constructor(
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase
): ViewModel() {

    fun getMovieReviews(movieId: Int) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())

            val response = getMovieReviewsUseCase(movieId)
            emit(StateView.Success(response))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }

    }

}