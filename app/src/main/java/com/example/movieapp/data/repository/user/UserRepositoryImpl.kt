package com.example.movieapp.data.repository.user

import com.example.movieapp.domain.model.user.User
import com.example.movieapp.domain.repository.user.UserRepository
import com.example.movieapp.util.IFirebaseHelper
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor (
    private val firebaseDatabase: FirebaseDatabase,
    private val firebaseHelper: IFirebaseHelper
): UserRepository {

    private val profileRef = firebaseDatabase.reference
        .child("profile")

    override suspend fun update(user: User) {
       return suspendCancellableCoroutine { continuation ->
           profileRef
               .child(firebaseHelper.getUserId())
               .setValue(user)
               .addOnCompleteListener { task ->
                   if (continuation.isActive){
                       if (task.isSuccessful) {
                           continuation.resumeWith(Result.success(Unit))
                       } else {
                           continuation.resumeWith(Result.failure(Exception()))
                       }
                   }
               }
       }
    }
}