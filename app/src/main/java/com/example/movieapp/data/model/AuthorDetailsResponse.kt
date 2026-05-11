package com.example.movieapp.data.model

import kotlinx.serialization.SerialName

data class AuthorDetailsResponse(
    @SerialName("name")
    val name: String?,

    @SerialName("username")
    val username: String?,

    @SerialName("avatar_path")
    val avatarPath: String?,

    @SerialName("rating")
    val rating: Int?
)
