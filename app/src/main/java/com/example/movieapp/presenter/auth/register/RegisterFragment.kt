package com.example.movieapp.presenter.auth.register

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
import com.example.movieapp.databinding.FragmentRegisterBinding
import com.example.movieapp.util.StateView
import com.example.movieapp.util.hideKeyboard
import com.google.android.material.internal.ViewUtils.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterFragment : Fragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()


    }

    private fun initListeners() {

        binding.toolbar2.setNavigationOnClickListener {
            findNavController().popBackStack()
        }


        binding.btnRegister.setOnClickListener {
            validateDate()
        }

        Glide.with(requireContext())
            .load(R.drawable.ic_loading)
            .into(binding.progressLoading);
    }

    private fun validateDate() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()

        when {
            email.isEmpty() -> ""
            password.isEmpty() -> ""

            else -> {
                hideKeyboard()
                registerUser(email, password)
            }
        }
    }

    private fun registerUser(email: String, password: String) {
        viewModel.register(email, password).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressLoading.visibility = View.VISIBLE
                }

                is StateView.Success -> {
                    binding.progressLoading.visibility = View.GONE
                    Toast.makeText(requireContext(), "Cadastro realizado com sucesso", Toast.LENGTH_SHORT).show()
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