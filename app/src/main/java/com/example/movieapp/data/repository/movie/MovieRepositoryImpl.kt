package com.example.movieapp.data.repository.movie

import com.example.movieapp.data.api.ServiceApi
import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.data.mapper.toPresentation
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Pagination
import com.example.movieapp.domain.repository.movie.MovieRepository
import com.example.movieapp.presenter.model.GenrePresentation
import jakarta.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val serviceApi: ServiceApi
): MovieRepository {
    override suspend fun getGenres(): List<GenrePresentation> {
        // Busca os dados e já mapeia para a lista de Presentation
        val response = serviceApi.getGenres()
        return response.toPresentation()
    }

    override suspend fun getMoviesByGenre(genreId: Int): Pagination<Movie> {
        // Chama a API de Discover (Retorna BasePaginationRemote<MovieResponse>)
        val response = serviceApi.getMovieByGenre(genreId)

        // Usa o Mapper para converter o envelope de paginação e a lista de filmes
        return response.toDomain()
    }
}