package com.example.movieapp.data.repository.user

import com.example.movieapp.domain.model.user.User
import com.example.movieapp.domain.repository.user.UserRepository
import com.example.movieapp.util.IFirebaseHelper
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

    override suspend fun getUser(): User? {
        return suspendCancellableCoroutine { continuation ->
            profileRef
                .child(firebaseHelper.getUserId())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {

                        if (continuation.isActive) {
                            val user = snapshot.getValue(User::class.java)
                            continuation.resumeWith(Result.success(user))
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
