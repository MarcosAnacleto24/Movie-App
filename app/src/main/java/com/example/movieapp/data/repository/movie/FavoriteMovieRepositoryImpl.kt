package com.example.movieapp.data.repository.movie

import com.example.movieapp.domain.model.favorite.FavoriteMovie
import com.example.movieapp.domain.repository.movie.FavoriteMovieRepository
import com.example.movieapp.util.IFirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class FavoriteMovieRepositoryImpl @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseHelper: IFirebaseHelper
): FavoriteMovieRepository {
    private val favoritesRef
        get() = firebaseDatabase.reference
            .child("favorites")
            .child(firebaseHelper.getUserId())

    override suspend fun saveFavoriteMovie(movie: FavoriteMovie) {
        val movieId = movie.id ?: return
        return suspendCancellableCoroutine { continuation ->
            favoritesRef
                .child(movieId.toString())
                .setValue(movie)
                .addOnCompleteListener { task ->
                    if (continuation.isActive) {
                        if (task.isSuccessful) {
                            continuation.resumeWith(Result.success(Unit))
                        } else {
                            continuation.resumeWith(Result.failure(task.exception ?: Exception()))
                        }
                    }
                }
        }
    }

    override suspend fun removeFavoriteMovie(movieId: Int) {
        return suspendCancellableCoroutine { continuation ->
            favoritesRef
                .child(movieId.toString())
                .removeValue()
                .addOnCompleteListener { task ->
                    if (continuation.isActive) {
                        if (task.isSuccessful) {
                            continuation.resumeWith(Result.success(Unit))
                        } else {
                            continuation.resumeWith(Result.failure(task.exception ?: Exception()))
                        }
                    }
                }
        }
    }

    override suspend fun getFavoriteMovies(): List<FavoriteMovie> {
        return suspendCancellableCoroutine { continuation ->
            favoritesRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (continuation.isActive) {
                        val favoritesList = mutableListOf<FavoriteMovie>()
                        for (item in snapshot.children) {
                            val favorite = item.getValue(FavoriteMovie::class.java)
                            favorite?.let { favoritesList.add(it) }
                        }
                        continuation.resumeWith(Result.success(favoritesList))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    if (continuation.isActive) {
                        continuation.resumeWith(Result.failure(error.toException()))
                    }
                }
            })
        }
    }

    override suspend fun isFavoriteMovie(movieId: Int): Boolean {
        return suspendCancellableCoroutine { continuation ->
            favoritesRef.child(movieId.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (continuation.isActive) {
                            continuation.resumeWith(Result.success(snapshot.exists()))
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        if (continuation.isActive) {
                            continuation.resumeWith(Result.failure(error.toException()))
                        }
                    }
                })
        }
    }
}