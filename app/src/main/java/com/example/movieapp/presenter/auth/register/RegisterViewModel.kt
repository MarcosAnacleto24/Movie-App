package com.example.movieapp.presenter.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.R
import com.example.movieapp.domain.usecase.auth.RegisterUseCase
import com.example.movieapp.util.StateView
import com.google.firebase.auth.FirebaseAuthException
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
        } catch (e: FirebaseAuthException) {
            e.printStackTrace()

            val errorMessage = when(e.errorCode) {
                "ERROR_WEAK_PASSWORD" -> R.string.enter_password_stronger
                "ERROR_INVALID_EMAIL" -> R.string.email_invalid
                "ERROR_EMAIL_ALREADY_IN_USE" -> R.string.this_email_used
                else -> R.string.error_register

            }

            emit(StateView.Error(stringResId = errorMessage))
        }

    }
}