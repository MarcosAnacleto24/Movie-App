package com.example.movieapp.presenter.main.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.databinding.FragmentSimilarBinding
import com.example.movieapp.presenter.main.movie_details.adapter.MovieSimilarAdapter
import com.example.movieapp.util.StateView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SimilarFragment : Fragment() {
    private var _binding: FragmentSimilarBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieSimilarAdapter: MovieSimilarAdapter

    private val movieDetailsViewModel: MovieDetailsViewModel by activityViewModels()
    private val similarViewModel: SimilarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSimilarBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        initObservers()

    }

    private fun initObservers() {
        movieDetailsViewModel.movieId.observe(viewLifecycleOwner) { movieId ->
            getSimilar(movieId)
        }
    }

    private fun initRecycler() {
        movieSimilarAdapter = MovieSimilarAdapter(requireContext()) { movieId ->

            movieId?.let {
                val action = MainGraphDirections.actionGlobalMovieDetailsFragment(movieId)
                findNavController().navigate(action)
            }
        }


       with(binding.recyclerMovies) {
            adapter = movieSimilarAdapter
            setHasFixedSize(true)
        }
    }

    private fun getSimilar(movieId: Int) {
        similarViewModel.getMovieSimilar(movieId).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    // Show loading indicator
                }
                is StateView.Success -> {
                    val movies = stateView.data?.results
                    movieSimilarAdapter.submitList(movies)
                }
                is StateView.Error -> {
                    // Show error message
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}