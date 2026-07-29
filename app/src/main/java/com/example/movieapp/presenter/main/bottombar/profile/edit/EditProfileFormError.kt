package com.example.movieapp.presenter.main.bottombar.profile.edit

 sealed class EditProfileFormError {
     object EmptyFirstName : EditProfileFormError()
     object EmptyLastName : EditProfileFormError()
     object InvalidTelephone : EditProfileFormError()
     object EmptySex : EditProfileFormError()
     object EmptyCountry : EditProfileFormError()

}