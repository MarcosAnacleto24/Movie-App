package com.example.movieapp.domain.model

import android.os.Parcelable
import com.example.movieapp.data.model.PersonResponse
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Credit(
    val cast: List<Person>?
): Parcelable
