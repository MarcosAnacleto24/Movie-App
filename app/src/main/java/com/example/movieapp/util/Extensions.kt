package com.example.movieapp.util

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.navOptions
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.movieapp.R
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
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

fun Fragment.showSnackBarString(message: String, duration: Int = Snackbar.LENGTH_SHORT) {
    view?.let { Snackbar.make(it, message, duration).show() }
}

fun Context.circularProgressDrawable(
    strokeWidth: Float = 12f,   // Valor padrão para imagens grandes
    centerRadius: Float = 60f   // Valor padrão para imagens grandes
): Drawable {
    return CircularProgressDrawable(this).apply {
        this.strokeWidth = strokeWidth
        this.centerRadius = centerRadius
        setColorSchemeColors(
            ContextCompat.getColor(
                this@circularProgressDrawable,
                R.color.color_default
            )
        )
        start()
    }
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

fun formatCommentDate(date: String?): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
    val providedDate = date?.let { dateFormat.parse(it) }
    val currentDate = Date()

    val calendarProvided = Calendar.getInstance()
    val calendarCurrent = Calendar.getInstance()
    providedDate?.let { calendarProvided.time = it }
    calendarCurrent.time = currentDate

    val yearDifference = calendarCurrent.get(Calendar.YEAR) - calendarProvided.get(Calendar.YEAR)
    val monthDifference = calendarCurrent.get(Calendar.MONTH) - calendarProvided.get(Calendar.MONTH)
    val dayDifference = calendarCurrent.get(Calendar.DAY_OF_MONTH) - calendarProvided.get(Calendar.DAY_OF_MONTH)

    val totalDaysDifference = yearDifference * 365 + monthDifference * 30 + dayDifference

    return when {
        totalDaysDifference == 0 -> "Hoje"
        totalDaysDifference == 1 -> "Ontem"
        totalDaysDifference < 31 -> "$totalDaysDifference dias atrás"
        else -> {
            val monthsDifference = totalDaysDifference / 30
            if (monthsDifference == 1) {
                "1 mês atrás"
            } else {
                "$monthsDifference meses atrás"
            }
        }
    }
}

fun Double.calculateFileSize(): String {
    val value = this * 10.0

    return if (value >= 1000) {
        String.format(Locale.getDefault(),"%.2f GB", value / 1000)
    } else {
        String.format(Locale.getDefault(), "%.1f MB", value)
    }
}

fun Int.calculateMovieTime(): String {
    val hours = this / 60
    val minutes = this % 60

    return "${hours}h ${minutes}m"
}

fun NavController.animateNavigation(
    action: Int,
    navAnimationType: NavAnimationType = NavAnimationType.SLIDE_HORIZONTAL
) {
    val navOptions = getNavOptions(navAnimationType)
    this.navigate(action,null, navOptions)
}

fun NavController.animateNavigation(
    nav: NavDirections,
    animationType: NavAnimationType = NavAnimationType.SHARED_AXIS_X
) {
    val navOptions = getNavOptions(animationType)
    this.navigate(nav.actionId, nav.arguments, navOptions)
}

private fun getNavOptions(animationType: NavAnimationType): NavOptions {
    val builder = NavOptions.Builder()

    when (animationType) {
        NavAnimationType.SLIDE_HORIZONTAL -> {
            builder.setEnterAnim(R.anim.enter)
                .setExitAnim(R.anim.exit)
                .setPopEnterAnim(R.anim.pop_enter)
                .setPopExitAnim(R.anim.pop_exit)
        }
        NavAnimationType.SLIDE_VERTICAL -> {
            builder.setEnterAnim(R.anim.slide_up_enter)
                .setExitAnim(R.anim.slide_up_exit)
                .setPopEnterAnim(R.anim.slide_down_pop_enter)
                .setPopExitAnim(R.anim.slide_down_pop_exit)
        }
        NavAnimationType.FADE -> {
            builder.setEnterAnim(R.anim.fade_in)
                .setExitAnim(R.anim.fade_out)
                .setPopEnterAnim(R.anim.fade_in)
                .setPopExitAnim(R.anim.fade_out)
        }
        NavAnimationType.SHARED_AXIS_X -> {
            builder.setEnterAnim(R.anim.shared_axis_x_enter)
                .setExitAnim(R.anim.shared_axis_x_exit)
                .setPopEnterAnim(R.anim.shared_axis_x_enter)
                .setPopExitAnim(R.anim.shared_axis_x_exit)
        }
    }

    return builder.build()
}



