package com.example.movieapp.presenter.auth.forgot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.R
import com.example.movieapp.domain.usecase.auth.ForgotUseCase
import com.example.movieapp.util.StateView
import com.google.firebase.auth.FirebaseAuthException
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
        } catch (e: FirebaseAuthException) {
            e.printStackTrace()

            val errorMessage = when(e.errorCode) {
                "ERROR_USER_NOT_FOUND" -> R.string.error_user_not_found
                "ERROR_INVALID_EMAIL" -> R.string.email_invalid
                else -> R.string.error_generic
            }

            emit(StateView.Error(stringResId = errorMessage))
        }

    }
}