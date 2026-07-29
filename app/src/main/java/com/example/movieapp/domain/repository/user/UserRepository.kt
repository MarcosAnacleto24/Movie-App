package com.example.movieapp.domain.repository.user

import android.net.Uri
import com.example.movieapp.domain.model.user.User

interface UserRepository {

    suspend fun update(user: User)

    suspend fun getUser(): User?

    suspend fun uploadProfileImage(imageUri: Uri): String
}