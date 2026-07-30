package com.example.movieapp.presenter.main.bottombar.profile

import androidx.lifecycle.ViewModel
import com.example.movieapp.domain.model.user.User
import com.example.movieapp.domain.usecase.user.GetUserUseCase
import com.example.movieapp.util.IFirebaseHelper
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val firebaseHelper: IFirebaseHelper
): ViewModel() {

    fun getUser(): Flow<StateView<User?>> = flow {
        emit(StateView.Loading())

        try {
            val user = getUserUseCase()

            emit(StateView.Success(user))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }

    }.flowOn(Dispatchers.IO)

    fun getUserEmail(): String {
        return firebaseHelper.getUserEmail()
    }
}