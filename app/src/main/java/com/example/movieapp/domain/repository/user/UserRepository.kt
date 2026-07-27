package com.example.movieapp.domain.repository.user

import com.example.movieapp.domain.model.user.User

interface UserRepository {

    suspend fun update(user: User)

    suspend fun getUser(): User?
}