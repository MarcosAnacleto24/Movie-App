package com.example.movieapp.util

import com.google.firebase.auth.FirebaseAuth

interface IFirebaseHelper {
    fun getAuth(): FirebaseAuth
    fun isAuthenticated(): Boolean
    fun getUserId(): String
}