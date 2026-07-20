package com.example.movieapp.presenter.main.bottombar.home

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
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.databinding.FragmentHomeBinding
import com.example.movieapp.presenter.main.bottombar.home.adapter.GenreMovieAdapter
import com.example.movieapp.util.StateView
import com.example.movieapp.util.animateNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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

        observeGenres()

        viewModel.fetchGenresIfNeeded()

    }

    private fun observeGenres() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.genresState.collectLatest { stateView ->
                    when (stateView) {
                        is StateView.Loading -> {
                            binding.shimmerViewContainer.visibility = View.VISIBLE
                            binding.shimmerViewContainer.startShimmer()
                            binding.rvHome.visibility = View.GONE
                        }

                        is StateView.Success -> {
                            binding.shimmerViewContainer.stopShimmer()
                            binding.shimmerViewContainer.visibility = View.GONE
                            binding.rvHome.visibility = View.VISIBLE

                            stateView.data?.let { genres ->
                                genreMovieAdapter.submitList(genres)
                            }
                        }

                        is StateView.Error -> {
                            binding.shimmerViewContainer.stopShimmer()
                            binding.shimmerViewContainer.visibility = View.GONE
                        }
                    }
                }
            }
        }
    }


    private fun configRecyclerView() {

        genreMovieAdapter = GenreMovieAdapter(
            showAll = { genreId, genreName ->
                val action = HomeFragmentDirections.actionMenuHomeToMovieGenreFragment(genreId, genreName)
                findNavController().animateNavigation(action)
            },
            onClick = { movieId ->
                movieId?.let {
                    val action = MainGraphDirections.actionGlobalMovieDetailsFragment(movieId)
                    findNavController().animateNavigation(action)
                }


            }
        )

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