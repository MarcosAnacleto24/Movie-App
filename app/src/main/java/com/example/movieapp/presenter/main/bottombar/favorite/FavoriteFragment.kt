package com.example.movieapp.presenter.main.bottombar.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import com.example.movieapp.MainGraphDirections.Companion.actionGlobalMovieDetailsFragment
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentFavoriteBinding
import com.example.movieapp.domain.model.favorite.FavoriteMovie
import com.example.movieapp.util.StateView
import com.example.movieapp.util.animateNavigation
import com.example.movieapp.util.hideKeyboard
import com.example.movieapp.util.initToolbar
import com.ferfalk.simplesearchview.SimpleSearchView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var mAdapter: FavoriteMovieAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupMenu()
        initToolbar(binding.toolbar, showIconNavigation = false)
        configRecyclerView()
        initObservers()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getFavoriteMovies()
    }

    private fun configRecyclerView() {
        mAdapter = FavoriteMovieAdapter(
            requireContext(),
            onClick = { movieId ->
                movieId?.let {
                    val action = actionGlobalMovieDetailsFragment(it)
                    findNavController().animateNavigation(action)
                }
            }
        )
        binding.rvMovies.adapter = mAdapter
    }

    private fun initObservers() {
        viewModel.favoriteList.observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.rvMovies.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.GONE
                    binding.layoutEmptyListMovies.visibility = View.GONE
                }

                is StateView.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val movies = stateView.data ?: emptyList()

                    if (!binding.simpleSearchView.isSearchOpen) {
                        mAdapter.submitList(movies)
                        handleEmptyStates(isSearchActive = false, isListEmpty = movies.isEmpty())
                    }
                }

                is StateView.Error -> {
                    binding.progressBar.visibility = View.GONE
                    handleEmptyStates(isSearchActive = false, isListEmpty = true)
                }
            }
        }

        viewModel.favoriteSearchList.observe(viewLifecycleOwner) { movies ->
            if (binding.simpleSearchView.isSearchOpen) {
                mAdapter.submitList(movies)
                handleEmptyStates(isSearchActive = true, isListEmpty = movies.isEmpty())
            }
        }
    }

    private fun initListeners() {
        initSearchView()
        onBackPressed()
    }


    private fun getCurrentFavorites(): List<FavoriteMovie> {
        return (viewModel.favoriteList.value as? StateView.Success)?.data ?: emptyList()
    }

    private fun initSearchView() {
        binding.simpleSearchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotBlank()) {
                    viewModel.searchMovie(newText)
                } else {
                    val currentList = getCurrentFavorites()
                    mAdapter.submitList(currentList)
                    handleEmptyStates(isSearchActive = false, isListEmpty = currentList.isEmpty())
                }
                return true
            }

            override fun onQueryTextCleared(): Boolean = false
        })

        binding.simpleSearchView.setOnSearchViewListener(object : SimpleSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                val currentList = getCurrentFavorites() // 👈 Extração segura
                mAdapter.submitList(currentList)
                handleEmptyStates(isSearchActive = false, isListEmpty = currentList.isEmpty())
                hideKeyboard()
            }

            override fun onSearchViewShown() {}
            override fun onSearchViewClosedAnimation() {}
            override fun onSearchViewShownAnimation() {}
        })
    }

    private fun handleEmptyStates(isSearchActive: Boolean, isListEmpty: Boolean) {
        when {
            isSearchActive && isListEmpty -> {
                binding.rvMovies.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.layoutEmptyListMovies.visibility = View.GONE
            }
            !isSearchActive && isListEmpty -> {
                binding.rvMovies.visibility = View.GONE
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutEmptyListMovies.visibility = View.VISIBLE
            }
            else -> {
                binding.rvMovies.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutEmptyListMovies.visibility = View.GONE
            }
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_search_view, menu)
                val item = menu.findItem(R.id.action_search)
                binding.simpleSearchView.setMenuItem(item)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean = true
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun onBackPressed() {
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (binding.simpleSearchView.isSearchOpen) {
                        binding.simpleSearchView.closeSearch()
                    } else {
                        findNavController().popBackStack()
                    }
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}