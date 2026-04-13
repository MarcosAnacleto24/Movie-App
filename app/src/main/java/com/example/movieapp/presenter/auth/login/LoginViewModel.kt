package com.example.movieapp.presenter.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.movieapp.R
import com.example.movieapp.domain.usecase.auth.LoginUseCase
import com.example.movieapp.util.StateView
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.Dispatchers

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {

    fun login(email: String, password: String) = liveData(Dispatchers.IO) {
        emit(StateView.Loading())
        try {
            loginUseCase(email, password)
            emit(StateView.Success(Unit))
        } catch (e: Exception) {
            e.printStackTrace()

            val errorMessage = when(e) {
                is FirebaseAuthInvalidCredentialsException, is FirebaseAuthInvalidUserException ->
                    R.string.account_not_register_or_password_invalid

                else -> R.string.error_generic
            }

            emit(StateView.Error(stringResId = errorMessage))
        }

    }
}