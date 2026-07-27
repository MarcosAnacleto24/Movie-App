package com.example.movieapp.domain.model.movie

data class Pagination<T>(
    val page: Int?,
    val results: List<T>?,
    val totalPages: Int?,
    val totalResults: Int?

)
