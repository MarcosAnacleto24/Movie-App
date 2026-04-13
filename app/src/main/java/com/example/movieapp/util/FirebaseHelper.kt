package com.example.movieapp.util

import com.google.firebase.auth.FirebaseAuth

object FirebaseHelper : IFirebaseHelper {

    override fun getAuth() = FirebaseAuth.getInstance()

    override fun isAuthenticated() = getAuth().currentUser != null

    override fun getUserId() = FirebaseAuth.getInstance().currentUser?.uid ?: ""


}