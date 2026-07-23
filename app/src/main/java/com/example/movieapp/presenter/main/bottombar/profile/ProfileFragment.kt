package com.example.movieapp.presenter.main.bottombar.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.movieapp.R
import com.example.movieapp.databinding.BottomSheetLogoutBinding
import com.example.movieapp.databinding.FragmentProfileBinding
import com.example.movieapp.presenter.auth.activity.AuthActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

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

        initListeners()

    }

    private fun initListeners() {

        binding.btnEditProfile.setOnClickListener {
            findNavController().navigate(R.id.action_menu_profile_to_editProfileFragment)
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