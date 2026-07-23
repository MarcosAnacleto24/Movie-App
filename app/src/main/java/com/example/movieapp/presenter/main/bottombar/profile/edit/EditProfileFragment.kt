package com.example.movieapp.presenter.main.bottombar.profile.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentEditProfileBinding
import com.example.movieapp.util.hideKeyboard
import com.example.movieapp.util.initToolbar
import com.example.movieapp.util.showSnackBar
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class EditProfileFragment : Fragment() {
    private var _binding: FragmentEditProfileBinding? = null
    private val binding get() = _binding!!

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

        initListeners()
    }

    private fun initUI() {
        setupUserData()
        setupSexDropdown()
        setupCountryDropdown()
    }

    private fun setupUserData() {
        val currentUser = FirebaseAuth.getInstance().currentUser
        binding.editEmail.setText(currentUser?.email ?: "")
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
        binding.btnUpdate.setOnClickListener {
            validateData()
        }
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
                updateUser(firstName, lastName, telephone, sex, country)
            }
        }

    }

    private fun updateUser(
        firstName: String,
        lastName: String,
        telephone: String,
        sex: String,
        country: String
    ) {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}