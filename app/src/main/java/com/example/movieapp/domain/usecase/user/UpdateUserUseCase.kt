package com.example.movieapp.domain.usecase.user

import com.example.movieapp.domain.model.user.User
import com.example.movieapp.domain.repository.user.UserRepository
import javax.inject.Inject

class UpdateUserUseCase @Inject constructor(
    private val userRepository: UserRepository
){
    suspend operator fun invoke(user: User) {
        userRepository.update(user)
    }
}