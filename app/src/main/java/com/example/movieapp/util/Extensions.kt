package com.example.movieapp.util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

fun Fragment.hideKeyboard() {
    val view = activity?.currentFocus
    if (view != null) {
        val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
        view.clearFocus()
    }
}

fun String.isEmailValid(): Boolean {
    val emailPattern = Regex("[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+")
    return emailPattern.matches(this)
}

fun Fragment.initToolbar(toolbar: Toolbar, showIconNavigation: Boolean = false) {
    (activity as AppCompatActivity).setSupportActionBar(toolbar)
    (activity as AppCompatActivity).title = ""

    if (showIconNavigation) {
        (activity as AppCompatActivity).supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_back)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    toolbar.setNavigationOnClickListener { activity?.onBackPressedDispatcher?.onBackPressed() }

}

fun Fragment.showSnackBar(message: Int, duration: Int = Snackbar.LENGTH_SHORT) {
    view?.let { Snackbar.make(it, message, duration).show() }
}

fun String.formatDate(): String? {
    return try {
        // Formato que vem da API NewsAPI
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        // Formato brasileiro que queremos exibir
        val outputFormat = SimpleDateFormat("yyyy", Locale.getDefault())

        val date = inputFormat.parse(this)
        date?.let { outputFormat.format(it) }
    } catch (_: Exception) {
        this // Se der erro, retorna o texto original para não quebrar o app
    }
}