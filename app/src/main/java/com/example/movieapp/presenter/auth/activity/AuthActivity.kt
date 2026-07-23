package com.example.movieapp.presenter.auth.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.movieapp.R
import com.example.movieapp.databinding.ActivityAuthBinding
import com.example.movieapp.presenter.main.activity.MainActivity
import com.example.movieapp.util.FirebaseHelper
import dagger.hilt.android.AndroidEntryPoint
import jakarta.inject.Inject

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() {

    @Inject
    lateinit var firebaseHelper: FirebaseHelper

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreenInstance = installSplashScreen()

        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // VERIFICA SE VEIO DA AÇÃO DE LOGOUT
        val showLogin = intent.getBooleanExtra("SHOW_LOGIN", false)

        if (showLogin) {
            // Se veio do logout, esconde a Splash e vai direto pro Login sem esperar animações
            binding.splashAnimationContainer.visibility = View.GONE
            binding.navHostFragment.visibility = View.VISIBLE
            binding.navHostFragment.alpha = 1f

            val navHostFragment = supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            // Configura a navegação para REMOVER a onboardingFragment da pilha
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.onboardingFragment, true) // 'true' remove o onboardingFragment da memória!
                .build()

            // Navega para o authentication aplicando as opções de remoção da pilha
            navController.navigate(R.id.authentication, null, navOptions)
        } else {
            // Fluxo normal com animação de Splash
            initAnimationSplash(splashScreenInstance)
        }

    }

    private fun initAnimationSplash(splashScreen: SplashScreen) {
        splashScreen.setOnExitAnimationListener { splashProvider ->
            // Mostra o fundo branco, a logo e o loading juntos para manter a transição fluida
            binding.splashAnimationContainer.visibility = View.VISIBLE
            binding.imgLogoSplash.visibility = View.VISIBLE
            binding.lottieSplashLoader.visibility = View.VISIBLE

            binding.lottieSplashLoader.playAnimation()

            // Remove a janela estática do sistema na hora
            splashProvider.remove()

            val isUserAuthenticated = firebaseHelper.isAuthenticated()

            // SE JÁ ESTIVER LOGADO: Antecipa a navegação um pouco antes do fim da animação!
            if (isUserAuthenticated) {
                binding.splashAnimationContainer.postDelayed({

                    // Anima o contêiner inteiro para sumir em Fade (fumaça)
                    binding.splashAnimationContainer.animate()
                        .alpha(0f)
                        .setDuration(300) // Transição rápida de 300 milissegundos
                        .withStartAction {
                            // Assim que o Fade começa, paramos o Lottie para ele não engasgar a CPU!
                            binding.lottieSplashLoader.cancelAnimation()
                        }
                        .start()

                    // Abre a MainActivity em paralelo
                    startActivity(Intent(this, MainActivity::class.java))

                    // Aplica a transição moderna para o Android 14+ ou fallback para os antigos
                    if (android.os.Build.VERSION.SDK_INT >= 34) {
                        overrideActivityTransition(
                            OVERRIDE_TRANSITION_OPEN, // OVERRIDE_TRANSITION_OPEN
                            android.R.anim.fade_in,
                            android.R.anim.fade_out
                        )
                    } else {
                        @Suppress("DEPRECATION")
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                }, 2700)

                // Fecha a AuthActivity aos 3000ms cravados
                binding.splashAnimationContainer.postDelayed({
                    finish()
                }, 3000)

            } else {
                // SE NÃO ESTIVER LOGADO: Segue o fluxo normal de 3 segundos para o Onboarding
                binding.splashAnimationContainer.postDelayed({
                    binding.lottieSplashLoader.cancelAnimation()

                    // Primeiro, deixa o NavHost invisível e com opacidade ZERO
                    binding.navHostFragment.visibility = View.VISIBLE
                    binding.navHostFragment.alpha = 0f

                    //  Anima a saída (Fade Out) da Logo e do Lottie simultaneamente
                    binding.imgLogoSplash.animate().alpha(0f).setDuration(500).start()
                    binding.lottieSplashLoader.animate().alpha(0f).setDuration(500).start()

                    //  Anima a entrada (Fade In) do contêiner de fundo e do NavHost
                    binding.splashAnimationContainer.animate()
                        .alpha(0f)
                        .setDuration(500)
                        .withEndAction {
                            // Quando a animação terminar por completo, limpamos o layout
                            binding.splashAnimationContainer.visibility = View.GONE
                            binding.imgLogoSplash.visibility = View.GONE
                            binding.lottieSplashLoader.visibility = View.GONE
                        }
                        .start()

                    binding.navHostFragment.animate()
                        .alpha(1f)
                        .setDuration(500)
                        .start()

                }, 3000)
            }
        }
    }
}