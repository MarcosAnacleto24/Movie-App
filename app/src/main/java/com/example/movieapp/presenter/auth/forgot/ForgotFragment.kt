package com.example.movieapp.presenter.auth.forgot

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentForgotBinding
import com.example.movieapp.util.StateView
import com.example.movieapp.util.hideKeyboard
import com.example.movieapp.util.initToolbar
import com.example.movieapp.util.isEmailValid
import com.example.movieapp.util.showSnackBar
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

        initToolbar(binding.toolbar)

        initListeners()


    }

    private fun initListeners() {

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
            !email.isEmailValid() -> showSnackBar(message = R.string.text_email_empty_forgot_fragment)

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
                    showSnackBar(message = R.string.text_email_sent_forgot_fragment)
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