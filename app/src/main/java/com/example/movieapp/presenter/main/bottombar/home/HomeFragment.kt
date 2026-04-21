package com.example.movieapp.presenter.main.bottombar.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.presenter.main.bottombar.home.adapter.GenreMovieAdapter
import com.example.movieapp.util.StateView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels()

    private lateinit var genreMovieAdapter: GenreMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configRecyclerView()

        getGenres()

    }

    private fun getGenres() {
        viewModel.getGenres().observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Success -> {

                    stateView.data?.let { genres ->
                        genreMovieAdapter.submitList(genres)
                    }
                }

                is StateView.Error -> {

                    // Exibir mensagem de erro, por exemplo, usando um Toast
                }
            }
        }
    }


    private fun configRecyclerView() {

        genreMovieAdapter = GenreMovieAdapter()

        with(binding.rvHome) {
            setHasFixedSize(true)
            adapter = genreMovieAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}