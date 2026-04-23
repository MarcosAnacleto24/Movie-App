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
import androidx.navigation.fragment.navArgs
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieGenreBinding
import com.example.movieapp.presenter.main.moviegenre.adapter.MovieGenreAdapter
import com.example.movieapp.util.StateView
import com.example.movieapp.util.hideKeyboard
import com.example.movieapp.util.initToolbar
import com.ferfalk.simplesearchview.SimpleSearchView
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
        viewModel.getMoviesByGenre(safeArgs.genreId).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {
                    binding.rvMovieGenre.visibility = View.GONE
                    binding.shimmerViewContainer.visibility = View.VISIBLE
                    binding.shimmerViewContainer.startShimmer()
                }

                is StateView.Success -> {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                    stateView.data?.let { movies ->
                        movieGenreAdapter.submitList(movies)
                    }
                    binding.rvMovieGenre.visibility = View.VISIBLE
                }

                is StateView.Error -> {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
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

    private fun initSearchView() {
        binding.simpleSearchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                if (query.isNotEmpty()) {
                    getMovies(query)
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

    private fun getMovies(query: String) {
        viewModel.searchMovies(query).observe(viewLifecycleOwner) { stateView ->
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
                    stateView.data?.let { movies ->
                        movieGenreAdapter.submitList(movies)
                    }


                }

                is StateView.Error -> {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}