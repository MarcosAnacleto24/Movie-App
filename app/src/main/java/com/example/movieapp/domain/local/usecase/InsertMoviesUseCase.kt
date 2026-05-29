package com.example.movieapp.domain.local.usecase

import com.example.movieapp.domain.local.repository.MovieLocalRepository
import com.example.movieapp.domain.model.Movie
import javax.inject.Inject

class InsertMoviesUseCase @Inject constructor(
    private val repository: MovieLocalRepository
) {
    suspend operator fun invoke(movie: Movie){
        repository.insertMovie(movie)

    }
}
