package com.example.movieapp.domain.model.favorite

import android.os.Parcelable
import com.example.movieapp.domain.model.movie.Country
import com.example.movieapp.domain.model.movie.Genre
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavoriteMovie(
    val genres: List<Genre>? = null,
    val id: Int? = null,
    val posterPath: String? = null,
    val title: String? = null,
    val voteAverage: Float? = null,
): Parcelable
