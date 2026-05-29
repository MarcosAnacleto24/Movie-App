package com.example.movieapp.presenter.main.bottombar.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.presenter.main.moviegenre.adapter.MovieGenreAdapter
import com.example.movieapp.util.StateView
import com.example.movieapp.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieGenreAdapter: MovieGenreAdapter

    private val viewModel: SearchViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configRecyclerView()

        initSearchView()

        initObservers()
    }

    private fun initObservers() {
        stateObserver()

        searchObserver()
    }

    private fun configRecyclerView() {

        movieGenreAdapter = MovieGenreAdapter(requireContext()) { movieId ->

            movieId?.let {
                val action = MainGraphDirections.actionGlobalMovieDetailsFragment(movieId)
                findNavController().navigate(action)
            }

        }

        with(binding.rvMovieGenre) {
            setHasFixedSize(true)
            adapter = movieGenreAdapter
        }
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                if (query.isNotEmpty()) {
                    viewModel.searchMovies(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }



        })


    }

    private fun stateObserver() {
        viewModel.searchState.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.shimmerViewContainer.visibility = View.VISIBLE
                    binding.shimmerViewContainer.startShimmer()
                    binding.rvMovieGenre.visibility = View.GONE
                }

                is StateView.Success -> {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.rvMovieGenre.visibility = View.VISIBLE


                }

                is StateView.Error -> {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                }
            }
        }
    }

    private fun searchObserver() {

        viewModel.movieList.observe(viewLifecycleOwner) { movieList ->
            movieGenreAdapter.submitList(movieList)

            emptyState(movieList.isEmpty())
        }


    }

    private fun emptyState(empty: Boolean) {
        binding.layoutEmpty.visibility = if (empty) View.VISIBLE else View.GONE
        binding.rvMovieGenre.visibility = if (empty) View.GONE else View.VISIBLE

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}