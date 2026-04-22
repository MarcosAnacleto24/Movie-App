package com.example.movieapp.presenter.main.moviegenre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.example.movieapp.databinding.FragmentMovieGenreBinding
import com.example.movieapp.presenter.main.moviegenre.adapter.MovieGenreAdapter
import com.example.movieapp.util.StateView
import com.example.movieapp.util.initToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieGenreFragment : Fragment() {
    private var _binding: FragmentMovieGenreBinding? = null
    private val binding get() = _binding!!

    private val safeArgs: MovieGenreFragmentArgs by navArgs()
    private val viewModel: MovieGenreViewModel by viewModels()
    private lateinit var movieGenreAdapter: MovieGenreAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieGenreBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)

        binding.textTitle.text = safeArgs.genreName

        configRecyclerView()

        getMovies()


    }

    private fun getMovies() {
        viewModel.getMoviesByGenre(safeArgs.genreId).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Success -> {
                    stateView.data?.let { movies ->
                        movieGenreAdapter.submitList(movies)
                    }
                }

                is StateView.Error -> {

                }
            }
        }
    }

    private fun configRecyclerView() {

        movieGenreAdapter = MovieGenreAdapter(requireContext())

        with(binding.rvMovieGenre) {
            setHasFixedSize(true)
            adapter = movieGenreAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}