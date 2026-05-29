package com.example.movieapp.domain.local.usecase

import com.example.movieapp.domain.local.repository.MovieLocalRepository
import javax.inject.Inject

class DeleteMoviesUseCase @Inject constructor(
    private val repository: MovieLocalRepository
) {
    suspend operator fun invoke(movieId: Int){
        repository.deleteMovie(movieId)

    }
}
