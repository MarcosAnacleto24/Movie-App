package com.example.movieapp.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.movieapp.data.api.ServiceApi
import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.domain.model.Movie

class MovieByGenrePagingSource (
   private val serviceApi: ServiceApi,
   private val genreId: Int,
) : PagingSource<Int, Movie>() {

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, Movie> {

       return try {
            val page = params.key ?: 1

            // Busca a resposta bruta da API (BasePaginationRemote<MovieResponse>)
            val response = serviceApi.getMovieByGenre(genreId, page = page)

            // Pega a lista bruta de MovieResponse
            val listRemote = response.results ?: emptyList()

            // Converte a lista bruta para a lista limpa (Domain) usando seu Mapper
            val cleanMovieList = listRemote.map { it.toDomain() }

            // Devolve para o Paging 3 a lista já perfeitamente limpa e pronta para a UI
            LoadResult.Page(
                data = cleanMovieList,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (cleanMovieList.isEmpty()) null else page + 1
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