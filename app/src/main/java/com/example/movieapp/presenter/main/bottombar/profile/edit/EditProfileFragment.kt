package com.example.movieapp.presenter.main.bottombar.profile.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.BottomSheetSelectImageBinding
import com.example.movieapp.databinding.FragmentEditProfileBinding
import com.example.movieapp.domain.model.user.User
import com.example.movieapp.util.StateView
import com.example.movieapp.util.hideKeyboard
import com.example.movieapp.util.initToolbar
import com.example.movieapp.util.showSnackBar
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale
import kotlin.time.Duration.Companion.milliseconds

@AndroidEntryPoint
class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditProfileViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)

        initUI()

        getUser()

        initListeners()
    }

    private fun initUI() {
        setupUserData()
        setupSexDropdown()
        setupCountryDropdown()
    }

    private fun setupUserData() {
        binding.editEmail.setText(viewModel.getUserEmail())
    }

    private fun setupSexDropdown() {
        val sexOptions = arrayOf(
            getString(R.string.text_option_male),               // Masculino
            getString(R.string.text_option_female),             // Feminino
            getString(R.string.text_option_prefer_not_to_say)  // Prefiro não responder
        )

        val adapter =
            ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                sexOptions
            )
        binding.spinnerSex.setAdapter(adapter)

    }

    private fun setupCountryDropdown() {
        val countriesList = getCountriesList()
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            countriesList
        )
        binding.spinnerCountry.setAdapter(adapter)

    }

    private fun getCountriesList(): List<String> {
        val currentLocale = Locale.getDefault()
        return Locale.getISOCountries()
            .map { code ->
                Locale.Builder().setRegion(code).build().getDisplayCountry(currentLocale)
            }
            .filter { it.isNotBlank() }
            .distinct()
            .sorted()
    }

    private fun initListeners() {

        binding.btnEditProfileImage.setOnClickListener {
            showBottomSheetSelectImage()
        }

        binding.btnUpdate.setOnClickListener {
            validateData()
        }

        Glide.with(requireContext())
            .load(R.drawable.ic_loading)
            .into(binding.progressLoading)
    }

    private fun validateData() {

        val firstName = binding.editFirstName.text.toString().trim()
        val lastName = binding.editLastName.text.toString().trim()
        val telephone = binding.editTelephone.unMaskedText.toString().trim()
        val sex = binding.spinnerSex.text.toString().trim()
        val country = binding.spinnerCountry.text.toString().trim()

        when {
            firstName.isEmpty() -> showSnackBar(message = R.string.text_first_name_empty_profile_edit_fragment)
            lastName.isEmpty() -> showSnackBar(message = R.string.text_last_name_empty_profile_edit_fragment)
            telephone.length != 11 -> showSnackBar(message = R.string.text_telephone_empty_or_invalid_profile_edit_fragment)
            sex.isEmpty() -> showSnackBar(message = R.string.text_sex_empty_profile_edit_fragment)
            country.isEmpty() -> showSnackBar(message = R.string.text_country_empty_profile_edit_fragment)

            else -> {
                hideKeyboard()

                val user = User(
                    firstName = firstName,
                    lastName = lastName,
                    email = viewModel.getUserEmail(),
                    telephone = telephone,
                    sex = sex,
                    country = country
                )

                updateUser(user)
            }
        }

    }

    private fun showBottomSheetSelectImage() {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        val bottomSheetBinding = BottomSheetSelectImageBinding.inflate(layoutInflater, null, false)

        bottomSheetBinding.btnTakePhoto.setOnClickListener {
            bottomSheetDialog.dismiss()
            // Lógica para tirar uma foto
        }

        bottomSheetBinding.btnChooseGallery.setOnClickListener {
            bottomSheetDialog.dismiss()
            // Lógica para escolher uma imagem da galeria
        }


        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun updateUser(user: User) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.updateUser(user).collect { stateView ->
                    when (stateView) {
                        is StateView.Loading -> {
                            binding.progressLoading.visibility = View.VISIBLE
                        }
                        is StateView.Success -> {
                            binding.progressLoading.visibility = View.GONE
                            showSnackBar(message = R.string.text_update_success_profile_edit_fragment)

                            delay(1000.milliseconds)

                            findNavController().popBackStack()
                        }
                        is StateView.Error -> {
                            binding.progressLoading.visibility = View.GONE
                            showSnackBar(message = R.string.text_update_error_profile_edit_fragment)
                        }
                    }
                }
            }
        }
    }

    private fun getUser() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getUser().collect { stateView ->
                    when (stateView) {
                        is StateView.Loading -> {
                            binding.progressLoading.visibility = View.VISIBLE
                        }

                        is StateView.Success -> {
                            binding.progressLoading.visibility = View.GONE
                            stateView.data?.let { user ->
                                configData(user)
                            }
                        }

                        is StateView.Error -> {
                            binding.progressLoading.visibility = View.GONE
                            showSnackBar(message = R.string.text_get_user_error_profile_edit_fragment)
                        }
                    }
                }
            }
        }

    }

    private fun configData(user: User) {
        binding.editFirstName.setText(user.firstName)
        binding.editLastName.setText(user.lastName)
        binding.editTelephone.setText(user.telephone)
        binding.spinnerSex.setText(user.sex, false)
        binding.spinnerCountry.setText(user.country, false)

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}