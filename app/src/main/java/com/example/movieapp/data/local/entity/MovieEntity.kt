package com.example.movieapp.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.movieapp.util.Columns
import com.example.movieapp.util.Tables

@Entity(tableName = Tables.MOVIE_TABLE)
data class MovieEntity(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(Columns.MOVIE_ID_COLUMN)
    val id: Int?,

    @ColumnInfo(Columns.MOVIE_TITLE_COLUMN)
    val title: String?,

    @ColumnInfo(Columns.MOVIE_POSTER_COLUMN)
    val poster: String?,

    @ColumnInfo(Columns.MOVIE_RUNTIME_COLUMN)
    val runtime: Int?,

    @ColumnInfo(Columns.MOVIE_INSERTION_COLUMN)
    val insertion: Long?
)
