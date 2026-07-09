package com.example.movieapp.domain.usecase.movie

import androidx.paging.PagingData
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.repository.movie.MovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(
    private val movieRepository: MovieRepository
) {
    // Retorna um fluxo reativo contínuo de PagingData limpo da camada Domain
    operator fun invoke(genreId: Int): Flow<PagingData<Movie>> {
        return movieRepository.getMoviesByGenre(genreId)
    }
}
