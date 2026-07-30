package com.example.movieapp.presenter.main.bottombar.profile.edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.domain.model.user.User
import com.example.movieapp.domain.usecase.user.GetUserUseCase
import com.example.movieapp.domain.usecase.user.UpdateUserUseCase
import com.example.movieapp.domain.usecase.user.UploadProfileImageUseCase
import com.example.movieapp.util.IFirebaseHelper
import com.example.movieapp.util.StateView
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userUpdateUserUseCase: UpdateUserUseCase,
    private val uploadProfileImageUseCase: UploadProfileImageUseCase,
    private val firebaseHelper: IFirebaseHelper,
    private val getUserUseCase: GetUserUseCase
): ViewModel() {

    private var currentUser: User? = null
    private val _formError = MutableSharedFlow<EditProfileFormError?>()
    val formError = _formError.asSharedFlow()

    private val _updateUserState = MutableStateFlow<StateView<Unit>?>(null)
    val updateUserState = _updateUserState.asStateFlow()

    fun validateAndUpdateUser(
        firstName: String,
        lastName: String,
        telephone: String,
        sex: String,
        country: String,
        imageUri: Uri?
    ) {
        viewModelScope.launch {
            when {
                firstName.isEmpty() -> {
                    _formError.emit(EditProfileFormError.EmptyFirstName)
                }
                lastName.isEmpty() -> {
                    _formError.emit(EditProfileFormError.EmptyLastName)
                }
                telephone.length != 11 -> {
                    _formError.emit(EditProfileFormError.InvalidTelephone)
                }

                sex.isEmpty() -> {
                    _formError.emit(EditProfileFormError.EmptySex)
                }

                country.isEmpty() -> {
                    _formError.emit(EditProfileFormError.EmptyCountry)
                }

                else -> {
                    val user = User(
                        id = getUserId(),
                        firstName = firstName,
                        lastName = lastName,
                        email = getUserEmail(),
                        telephone = telephone,
                        sex = sex,
                        country = country,
                        photoUrl = currentUser?.photoUrl
                    )

                    if (imageUri != null) {
                        uploadImageAndSaveUser(user, imageUri)
                    } else {
                        updateUser(user)
                    }

                }
            }
        }
    }

    private fun uploadImageAndSaveUser(user: User, imageUri: Uri) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserState.value = StateView.Loading()
            try {
                // Chamada do UseCase/Repository que faz upload no Storage
                 val photoUrl = uploadProfileImageUseCase(imageUri)
                 val updatedUser = user.copy(photoUrl = photoUrl)

                 userUpdateUserUseCase(updatedUser)

                _updateUserState.value = StateView.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                _updateUserState.value = StateView.Error(e.message)
            }
        }
    }

    private fun updateUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            _updateUserState.value = StateView.Loading()
            try {
                userUpdateUserUseCase(user)
                _updateUserState.value = StateView.Success(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                _updateUserState.value = StateView.Error(e.message)
            }
        }
    }

    fun getUserId(): String {
        return firebaseHelper.getUserId()
    }

    fun getUserEmail(): String {
        return firebaseHelper.getUserEmail()
    }


    fun getUser(): Flow<StateView<User?>> = flow {
        emit(StateView.Loading())

        try {
            val user = getUserUseCase()

            if (user != null) {
                currentUser = user
            }

            emit(StateView.Success(user))

        } catch (e: Exception) {
            e.printStackTrace()
            emit(StateView.Error(e.message))
        }

    }.flowOn(Dispatchers.IO)
}