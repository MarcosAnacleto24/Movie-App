package com.example.movieapp.domain.usecase.movie

import androidx.paging.PagingData
import com.example.movieapp.domain.model.movie.Movie
import com.example.movieapp.domain.repository.movie.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchMoviesUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
     operator fun invoke(query: String): Flow<PagingData<Movie>> {
        return movieRepository.searchMovies(query)
    }
}
