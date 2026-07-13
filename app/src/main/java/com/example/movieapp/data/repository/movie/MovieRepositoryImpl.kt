package com.example.movieapp.data.repository.movie

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.movieapp.data.api.ServiceApi
import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.data.mapper.toPresentation
import com.example.movieapp.data.paging.MovieByGenrePagingSource
import com.example.movieapp.data.paging.SearchMoviesPagingSource
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Pagination
import com.example.movieapp.domain.repository.movie.MovieRepository
import com.example.movieapp.presenter.model.GenrePresentation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class MovieRepositoryImpl @Inject constructor(
    private val serviceApi: ServiceApi
): MovieRepository {
    override suspend fun getGenres(): List<GenrePresentation> {
        // Busca os dados e já mapeia para a lista de Presentation
        val response = serviceApi.getGenres()
        return response.toPresentation()
    }

    override fun getMoviesByGenre(genreId: Int): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,          // Quantidade de itens por página da API do TMDB
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                // Passamos a fiação para a PagingSource de filmes por gênero
                MovieByGenrePagingSource(serviceApi = serviceApi, genreId = genreId)
            }
        ).flow // Retorna o fluxo reativo puro
    }

    override fun searchMovies(query: String): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                // Passamos a fiação para a PagingSource de busca
                SearchMoviesPagingSource(serviceApi = serviceApi, query = query)
            }
        ).flow
    }

    override suspend fun getMoviesByGenreList(genreId: Int): Pagination<Movie> {
        val response = serviceApi.getMovieByGenre(genreId = genreId, page = 1)
        return response.toDomain()
    }

}