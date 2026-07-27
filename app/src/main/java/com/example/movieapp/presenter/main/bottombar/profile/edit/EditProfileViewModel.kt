package com.example.movieapp.presenter.main.bottombar.profile.edit

import androidx.lifecycle.ViewModel
import com.example.movieapp.domain.model.user.User
import com.example.movieapp.domain.usecase.user.GetUserUseCase
import com.example.movieapp.domain.usecase.user.UpdateUserUseCase
import com.example.movieapp.util.StateView
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userUpdateUserUseCase: UpdateUserUseCase,
    private val firebaseAuth: FirebaseAuth,
    private val userGetUserUseCase: GetUserUseCase
): ViewModel() {

    fun getUserEmail(): String {
        return firebaseAuth.currentUser?.email ?: ""
    }

    fun updateUser(user: User): Flow<StateView<Unit>> = flow  {
        emit(StateView.Loading())

        try {
            userUpdateUserUseCase(user)

            emit(StateView.Success(Unit))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }
    }.flowOn(Dispatchers.IO) // Garante a execução na thread I/O


    fun getUser(): Flow<StateView<User?>> = flow {
        emit(StateView.Loading())

        try {
            val user = userGetUserUseCase()

            emit(StateView.Success(user))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }

    }.flowOn(Dispatchers.IO)
}