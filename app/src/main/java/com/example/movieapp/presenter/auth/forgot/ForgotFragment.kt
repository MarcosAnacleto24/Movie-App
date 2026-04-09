package com.example.movieapp.presenter.auth.forgot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentForgotBinding
import com.example.movieapp.util.StateView
import com.example.movieapp.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ForgotFragment : Fragment() {

    private var _binding: FragmentForgotBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ForgotViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentForgotBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()


    }

    private fun initListeners() {

        binding.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.btnForgot.setOnClickListener {
            validateDate()
        }

        Glide.with(requireContext())
            .load(R.drawable.ic_loading)
            .into(binding.progressLoading)
    }

    private fun validateDate() {
        val email = binding.editEmail.text.toString()

        when {
            email.isEmpty() -> ""

            else -> {
                hideKeyboard()
                loginUser(email)
            }
        }
    }

    private fun loginUser(email: String) {
        viewModel.forgot(email).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressLoading.visibility = View.VISIBLE
                }

                is StateView.Success -> {
                    binding.progressLoading.visibility = View.GONE
                    Toast.makeText(requireContext(), "Envio de link realizado com sucesso", Toast.LENGTH_SHORT).show()
                }

                is StateView.Error -> {
                    binding.progressLoading.visibility = View.GONE
                    Toast.makeText(requireContext(), stateView.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}