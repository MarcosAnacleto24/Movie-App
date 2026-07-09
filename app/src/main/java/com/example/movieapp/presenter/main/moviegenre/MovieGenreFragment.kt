package com.example.movieapp.presenter.main.moviegenre

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieGenreBinding
import com.example.movieapp.presenter.main.moviegenre.adapter.MovieGenreAdapter
import com.example.movieapp.util.hideKeyboard
import com.example.movieapp.util.initToolbar
import com.ferfalk.simplesearchview.SimpleSearchView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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

        binding.toolbar.title = safeArgs.genreName

        // Configura o Menu de forma moderna
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Infla o menu
                menuInflater.inflate(R.menu.menu_search_view, menu)

                val item = menu.findItem(R.id.action_search)
                binding.simpleSearchView.setMenuItem(item)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                // Trata cliques em outros itens de menu aqui, se houver
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        configRecyclerView()

        getMoviesByGenre()

        initSearchView()


    }

    private fun getMoviesByGenre() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.getMoviesByGenre(safeArgs.genreId).collect { pagingData ->
                // O metodo do PagingDataAdapter agora recebe a receita inteira e o ciclo de vida
                movieGenreAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }

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

        initAdapterLoadStateListener()

    }

    private fun initAdapterLoadStateListener() {
        movieGenreAdapter.addLoadStateListener { loadStates ->
            val refreshState = loadStates.refresh
            when (refreshState) {
                is LoadState.Loading -> {
                    binding.rvMovieGenre.visibility = View.GONE
                    binding.shimmerViewContainer.visibility = View.VISIBLE
                    binding.shimmerViewContainer.startShimmer()
                }
                is LoadState.NotLoading -> {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.rvMovieGenre.visibility = View.VISIBLE
                }
                is LoadState.Error -> {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE

                }
            }
        }
    }

    private fun initSearchView() {
        binding.simpleSearchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                if (query.isNotEmpty()) {
                    searchMovies(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
               return false
            }

            override fun onQueryTextCleared(): Boolean {
                return false
            }


        })

        binding.simpleSearchView.setOnSearchViewListener(object : SimpleSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                getMoviesByGenre()
            }

            override fun onSearchViewClosedAnimation() {
                // Não é necessário implementar nada aqui por enquanto
            }

            override fun onSearchViewShown() {
                // Não é necessário implementar nada aqui por enquanto
            }

            override fun onSearchViewShownAnimation() {
                // Não é necessário implementar nada aqui por enquanto
            }

        })

    }

    private fun searchMovies(query: String) {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchMovies(query).collect { pagingData ->
                movieGenreAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}