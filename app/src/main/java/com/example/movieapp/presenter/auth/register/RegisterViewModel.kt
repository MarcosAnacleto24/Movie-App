package com.example.movieapp.presenter.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.domain.usecase.auth.RegisterUseCase
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    fun register(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(StateView.Loading())
        try {
            registerUseCase(email, password)
            emit(StateView.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message ?: "Error"))
        }

    }
}