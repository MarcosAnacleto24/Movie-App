package com.example.movieapp.presenter.auth.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentLoginBinding
import com.example.movieapp.presenter.MainActivity
import com.example.movieapp.util.StateView
import com.example.movieapp.util.hideKeyboard
import com.example.movieapp.util.initToolbar
import com.example.movieapp.util.isEmailValid
import com.example.movieapp.util.showSnackBar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)

        initListeners()


    }

    private fun initListeners() {

        binding.btnLogin.setOnClickListener {
            validateDate()
        }

        binding.btnForgot.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_forgotFragment)
        }

        Glide.with(requireContext())
            .load(R.drawable.ic_loading)
            .into(binding.progressLoading)
    }

    private fun validateDate() {
        val email = binding.editEmail.text.toString()
        val password = binding.editPassword.text.toString()


        when {
            !email.isEmailValid() -> showSnackBar(message = R.string.text_email_empty_login_fragment)
            password.isEmpty() -> showSnackBar(message = R.string.text_password_empty_login_fragment)

            else -> {
                hideKeyboard()
                loginUser(email, password)
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        viewModel.login(email, password).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressLoading.visibility = View.VISIBLE
                }

                is StateView.Success -> {
                    binding.progressLoading.visibility = View.GONE

                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    requireActivity().finish()

                }

                is StateView.Error -> {
                    binding.progressLoading.visibility = View.GONE
                   showSnackBar(message = stateView.stringResId ?: R.string.error_generic)
                }
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}