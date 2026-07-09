package com.example.movieapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.data.api.ServiceApi
import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.model.Movie

class SearchMoviesPagingSource(
    private val serviceApi: ServiceApi,
    private val query: String
) : PagingSource<Int, Movie>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val response = serviceApi.searchMovies(query = query, page = page)
            val listRemote = response.results ?: emptyList()

            // Mapeia para o objeto limpo do Domain
            val cleanMoviesList = listRemote.map { it.toDomain() }

            LoadResult.Page(
                data = cleanMoviesList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (cleanMoviesList.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}