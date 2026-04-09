package com.example.movieapp.presenter.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentOnboardingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnboardingFragment : Fragment() {
    private var _binding: FragmentOnboardingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOnboardingBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configureSpaseNavBar()

        initListeners()

    }

    private fun configureSpaseNavBar() {
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

            // Aplica o padding na parte de baixo para o botão não ficar coberto
            view.setPadding(0, 0, 0, insets.bottom)

            windowInsets
        }


    }

    private fun initListeners() {

        binding.btnOnboarding.setOnClickListener {
            findNavController().navigate(R.id.action_onboardingFragment_to_authentication)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}