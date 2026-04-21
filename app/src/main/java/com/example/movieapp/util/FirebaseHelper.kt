package com.example.movieapp.util

import com.google.firebase.auth.FirebaseAuth
import jakarta.inject.Inject

class  FirebaseHelper @Inject constructor(
    private val auth: FirebaseAuth
) : IFirebaseHelper {

    override fun getAuth() = auth

    override fun isAuthenticated() = auth.currentUser != null

    override fun getUserId() = auth.currentUser?.uid ?: ""


}