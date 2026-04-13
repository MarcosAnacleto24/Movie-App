package com.example.movieapp.presenter.main.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.root.post {
            navController = findNavController(R.id.nav_host_fragment)
            initBottomNavigation(navController)
        }

    }

    private fun initBottomNavigation(navController: NavController) {

        binding.bottomNav.itemIconTintList = null

        binding.bottomNav.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.menu_home, R.id.menu_search, R.id.menu_download, R.id.menu_profile, R.id.menu_favorite -> {
                    binding.bottomNav.visibility = android.view.View.VISIBLE
                }
                else -> {
                    binding.bottomNav.visibility = android.view.View.GONE
                }
            }
        }
    }


}