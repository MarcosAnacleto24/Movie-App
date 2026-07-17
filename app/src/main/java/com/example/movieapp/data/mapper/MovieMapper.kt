package com.example.movieapp.data.mapper

import com.example.movieapp.data.local.entity.MovieEntity
import com.example.movieapp.data.model.AuthorDetailsResponse
import com.example.movieapp.data.model.BasePaginationRemote
import com.example.movieapp.data.model.CountryResponse
import com.example.movieapp.data.model.CreditResponse
import com.example.movieapp.data.model.GenreResponse
import com.example.movieapp.data.model.GenresResponse
import com.example.movieapp.data.model.MovieResponse
import com.example.movieapp.data.model.MovieReviewResponse
import com.example.movieapp.data.model.PersonResponse
import com.example.movieapp.domain.model.AuthorDetails
import com.example.movieapp.domain.model.Country
import com.example.movieapp.domain.model.Credit
import com.example.movieapp.domain.model.Genre
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.domain.model.MovieReview
import com.example.movieapp.domain.model.Pagination
import com.example.movieapp.domain.model.Person
import com.example.movieapp.presenter.model.GenrePresentation

fun GenreResponse.toPresentation(): GenrePresentation {
    return GenrePresentation(
        id = this.id,
        name = this.name,
        movies = emptyList() // Inicializa vazio; os filmes serão carregados depois
    )
}

fun GenreResponse.toDomain(): Genre {
    return Genre(
        id = this.id,
        name = this.name
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
        genres = this.genres?.map { it.toDomain() },
        backdropPath = "https://image.tmdb.org/t/p/w500${this.backdropPath}",
        originalLanguage = this.originalLanguage,
        originalTitle = this.originalTitle,
        video = this.video,
        adult = this.adult,
        posterPath = this.posterPath,
        productionCompanies = this.productionCountries?.map { it.toDomain() },
        runtime = this.runtime

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

fun CountryResponse.toDomain(): Country {
    return Country(
        name = this.name
    )
}

fun PersonResponse.toDomain(): Person {
    return Person(
        adult = this.adult,
        gender = this.gender,
        id = this.id,
        knownForDepartment = this.knownForDepartment,
        name = this.name,
        originalName = this.originalName,
        popularity = this.popularity,
        profilePath = this.profilePath,
        castId = this.castId,
        character = this.character,
        creditId = this.creditId,
        order = this.order
    )
}

fun CreditResponse.toDomain(): Credit {
    return Credit(
        cast = this.cast?.map { it.toDomain() }
    )
}

fun AuthorDetailsResponse.toDomain(): AuthorDetails {
    return AuthorDetails(
        name = this.name,
        username = this.username,
        avatarPath = this.avatarPath,
        rating = this.rating
    )
}

fun MovieReviewResponse.toDomain(): MovieReview {
    return MovieReview(
        author = this.author,
        authorDetails = this.authorDetails?.toDomain(),
        content = this.content,
        createdAt = this.createdAt,
        id = this.id,
        updatedAt = this.updatedAt,
        url = this.url
    )
}


fun BasePaginationRemote<MovieReviewResponse>.toReviewDomain(): Pagination<MovieReview> {
    return Pagination(
        page = this.page,
        totalPages = this.totalPages,
        totalResults = this.totalResults,
        results = this.results?.map { it.toDomain() }
    )
}

fun Movie.toEntity(): MovieEntity {
    return MovieEntity(
        id = this.id,
        title = this.title,
        poster = this.posterPath ?: "", // Use uma string vazia como valor padrão se posterPath for nulo
        runtime = this.runtime ?: 0, // Use 0 como valor padrão se runtime for nulo
        insertion = System.currentTimeMillis() // Armazena a data de inserção
    )
}

fun MovieEntity.toDomain(): Movie {
    return Movie(
        id = this.id,
        title = this.title,
        posterPath = this.poster,
        runtime = this.runtime
    )
}