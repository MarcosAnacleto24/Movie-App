package com.example.movieapp.domain.usecase.movie

import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Pagination
import com.example.movieapp.domain.repository.movie.MovieRepository
import javax.inject.Inject

class GetMoviesByGenreListUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    suspend operator fun invoke(genreId: Int): Pagination<Movie> {
        return movieRepository.getMoviesByGenreList(genreId)
    }
}