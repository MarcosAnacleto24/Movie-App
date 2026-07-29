package com.example.movieapp.domain.usecase.user

import android.net.Uri
import com.example.movieapp.domain.repository.user.UserRepository
import javax.inject.Inject

class UploadProfileImageUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(imageUri: Uri): String {
        return userRepository.uploadProfileImage(imageUri)
    }
}