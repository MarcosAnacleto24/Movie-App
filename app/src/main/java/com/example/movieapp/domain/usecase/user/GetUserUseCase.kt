package com.example.movieapp.domain.usecase.user

import com.example.movieapp.domain.model.user.User
import com.example.movieapp.domain.repository.user.UserRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(): User? {
        return userRepository.getUser()
    }
}