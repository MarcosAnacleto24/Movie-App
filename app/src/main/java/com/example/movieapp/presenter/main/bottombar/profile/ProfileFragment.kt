package com.example.movieapp.presenter.main.bottombar.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.BottomSheetLogoutBinding
import com.example.movieapp.databinding.FragmentProfileBinding
import com.example.movieapp.domain.model.user.User
import com.example.movieapp.presenter.auth.activity.AuthActivity
import com.example.movieapp.util.StateView
import com.example.movieapp.util.animateNavigation
import com.example.movieapp.util.circularProgressDrawable
import com.example.movieapp.util.showSnackBar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getUser()

        initListeners()

    }

    private fun getUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUser().collect { stateView ->
                    when (stateView) {
                        is StateView.Loading -> {

                        }

                        is StateView.Success -> {

                            stateView.data?.let { user ->
                                configData(user)
                            }

                        }

                        is StateView.Error -> {

                            showSnackBar(message = R.string.text_get_user_error_profile_fragment)
                        }
                    }

                }
            }

        }
    }


    private fun configData(user: User) {

        binding.txtUserName.text =
            getString(R.string.text_user_name_profile_fragment,
                user.firstName,
                    user.lastName
            )
        binding.txtEmailUser.text = viewModel.getUserEmail()

        if (!user.photoUrl.isNullOrEmpty()) {

            Glide.with(this)
                .load(user.photoUrl)
                .placeholder(binding.root.context.circularProgressDrawable())
                .error(R.drawable.image_profile_error)
                .into(binding.imageProfile)
        } else {

            binding.imageProfile.setImageResource(R.drawable.image_profile_error)
        }


    }

    private fun initListeners() {

        binding.btnEditProfile.setOnClickListener {
            findNavController().animateNavigation(R.id.action_menu_profile_to_editProfileFragment)
        }

        binding.btnNotification.setOnClickListener {

        }

        binding.btnDownload.setOnClickListener {
            activity?.findViewById<BottomNavigationView>(R.id.bottom_nav)?.selectedItemId =
                R.id.menu_download
        }

        binding.btnSecurity.setOnClickListener {

        }

        binding.btnLanguage.setOnClickListener {

        }

        binding.btnDarkMode.setOnClickListener {

        }

        binding.btnHelpCenter.setOnClickListener {

        }

        binding.btnPrivacyPolicy.setOnClickListener {

        }

        binding.btnLogout.setOnClickListener {
            showBottomSheetLogout()
        }
    }

    private fun showBottomSheetLogout() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        val bottomSheetBinding = BottomSheetLogoutBinding.inflate(layoutInflater, null, false)


        bottomSheetBinding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.btnConfirm.setOnClickListener {
            bottomSheetDialog.dismiss()

            logout()

        }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()

        val intent = Intent(requireContext(), AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            // Envia o aviso de que o usuário quer ir direto para o Login
            putExtra("SHOW_LOGIN", true)
        }

        startActivity(intent)
        activity?.finish()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}