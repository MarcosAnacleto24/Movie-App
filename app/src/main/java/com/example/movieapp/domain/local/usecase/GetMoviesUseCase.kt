package com.example.movieapp.domain.local.usecase

import com.example.movieapp.domain.local.repository.MovieLocalRepository
import com.example.movieapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesUseCase @Inject constructor(
    private val repository: MovieLocalRepository
) {
    operator fun invoke(): Flow<List<Movie>> {
        return repository.getMovies()
    }
}