package com.example.movieapp.presenter.main.moviedetails.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.data.mapper.toFavoriteMovie
import com.example.movieapp.domain.local.usecase.InsertMoviesUseCase
import com.example.movieapp.domain.model.movie.Movie
import com.example.movieapp.domain.usecase.favorite.IsFavoriteMovieUseCase
import com.example.movieapp.domain.usecase.favorite.RemoveFavoriteMovieUseCase
import com.example.movieapp.domain.usecase.favorite.SaveFavoriteMovieUseCase
import com.example.movieapp.domain.usecase.movie.GetMovieCreditsUseCase
import com.example.movieapp.domain.usecase.movie.GetMovieDetailsUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class MovieDetailsViewModel @Inject constructor(
    private val getMovieDetailsUseCase: GetMovieDetailsUseCase,
    private val getMovieCreditsUseCase: GetMovieCreditsUseCase,
    private val insertMoviesUseCase: InsertMoviesUseCase,
    private val saveFavoriteMovieUseCase: SaveFavoriteMovieUseCase,
    private val removeFavoriteMovieUseCase: RemoveFavoriteMovieUseCase,
    private val isFavoriteMovieUseCase: IsFavoriteMovieUseCase
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

    fun insertMovie(movie: Movie) = liveData(Dispatchers.IO)  {
        try {
            emit(StateView.Loading())

            insertMoviesUseCase(movie)


            emit(StateView.Success(Unit))


        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }
    }

    fun isFavoriteMovie(movieId: Int) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())
            val isFavorite = isFavoriteMovieUseCase(movieId)
            emit(StateView.Success(isFavorite))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }
    }

    fun saveFavoriteMovie(movie: Movie) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())
            saveFavoriteMovieUseCase(movie.toFavoriteMovie())
            emit(StateView.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }
    }

    fun removeFavoriteMovie(movieId: Int) = liveData(Dispatchers.IO) {
        try {
            emit(StateView.Loading())
            removeFavoriteMovieUseCase(movieId)
            emit(StateView.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }
    }

    fun setMovieId(movieId: Int) {
        _movieId.value = movieId
    }
}