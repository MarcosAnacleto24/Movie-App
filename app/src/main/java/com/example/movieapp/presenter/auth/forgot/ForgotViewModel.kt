package com.example.movieapp.presenter.auth.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.domain.usecase.auth.ForgotUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class ForgotViewModel @Inject constructor(
    private val forgotUseCase: ForgotUseCase
) : ViewModel() {

    fun forgot(email: String) = liveData(Dispatchers.IO) {
        emit(StateView.Loading())
        try {
            forgotUseCase(email)
            emit(StateView.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message ?: "Error"))
        }

    }
}