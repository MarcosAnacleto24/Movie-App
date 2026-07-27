package com.example.movieapp.domain.model.user

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: String? = null,
    val photoUrl: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val telephone: String? = null,
    val sex: String? = null,
    val country: String? = null

): Parcelable
