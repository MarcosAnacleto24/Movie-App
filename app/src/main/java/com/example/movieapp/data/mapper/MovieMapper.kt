package com.example.movieapp.data.mapper

import com.example.movieapp.data.model.BasePaginationRemote
import com.example.movieapp.data.model.GenreResponse
import com.example.movieapp.data.model.GenresResponse
import com.example.movieapp.data.model.MovieResponse
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.Pagination
import com.example.movieapp.presenter.model.GenrePresentation

fun GenreResponse.toPresentation(): GenrePresentation {
    return GenrePresentation(
        id = this.id,
        name = this.name,
        movies = emptyList() // Inicializa vazio; os filmes serão carregados depois
    )
}

// Converte a lista de gêneros (extraindo do GenresResponse)
fun GenresResponse.toPresentation(): List<GenrePresentation> {
    return this.genres.map { it.toPresentation() }
}

// Converte um único filme
fun MovieResponse.toDomain(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        overview = this.overview,
        releaseDate = this.releaseDate,
        voteAverage = this.voteAverage,
        voteCount = this.voteCount,
        popularity = this.popularity,
        genreIds = this.genreIds,
        backdropPath = "https://image.tmdb.org/t/p/w500${this.backdropPath}",
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        video = this.video,
        adult = this.adult,
        posterPath = this.posterPath
    )
}

// Converte a paginação de filmes
fun BasePaginationRemote<MovieResponse>.toDomain(): Pagination<Movie> {
    return Pagination(
        page = this.page,
        totalPages = this.totalPages,
        totalResults = this.totalResults,
        results = this.results?.map { it.toDomain() }

    )
}