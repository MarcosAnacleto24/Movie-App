package com.example.movieapp.data.local.repository

import com.example.movieapp.data.local.dao.MovieDao
import com.example.movieapp.data.local.entity.MovieEntity
import com.example.movieapp.data.mapper.toDomain
import com.example.movieapp.data.mapper.toEntity
import com.example.movieapp.domain.local.repository.MovieLocalRepository
import com.example.movieapp.domain.model.Movie
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class MovieLocalRepositoryImpl @Inject constructor(
    private val movieDao: MovieDao
) : MovieLocalRepository {
    override fun getMovies(): Flow<List<Movie>> {
        return movieDao.getMovies().map { movieList ->
            movieList.map { it.toDomain() }

        }
    }

    override suspend fun insertMovie(movie: Movie) {
        movieDao.insertMovie(movie.toEntity())
    }

    override suspend fun deleteMovie(movieId: Int) {
        movieDao.deleteMovie(movieId)
    }


}