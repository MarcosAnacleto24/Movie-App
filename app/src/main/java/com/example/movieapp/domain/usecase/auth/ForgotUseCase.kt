package com.example.movieapp.domain.usecase.auth

import com.example.movieapp.domain.repository.auth.FirebaseAuthentication
import jakarta.inject.Inject

class ForgotUseCase @Inject constructor(
    private val firebaseAuthentication: FirebaseAuthentication

) {
    suspend operator fun invoke(email: String) {
        return firebaseAuthentication.forgot(email)
    }
}