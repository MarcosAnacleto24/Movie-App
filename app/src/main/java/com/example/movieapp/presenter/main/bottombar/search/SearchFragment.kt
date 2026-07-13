package com.example.movieapp.presenter.main.bottombar.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.paging.PagingData
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.databinding.FragmentSearchBinding
import com.example.movieapp.presenter.main.moviegenre.adapter.MovieGenreAdapter
import com.example.movieapp.util.hideKeyboard
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

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

        initSearchCollector()

        initSearchView()

        handleEmptyStates(isSearchActive = false, isListEmpty = true)

    }

    private fun configRecyclerView() {

        movieGenreAdapter = MovieGenreAdapter(requireContext()) { movieId ->

            movieId?.let {
                val action = MainGraphDirections.actionGlobalMovieDetailsFragment(movieId)
                findNavController().navigate(action)
            }

        }

        with(binding.rvMovie) {
            setHasFixedSize(true)
            adapter = movieGenreAdapter
        }

        initAdapterLoadStateListener()
    }

    private fun initAdapterLoadStateListener() {
        movieGenreAdapter.addLoadStateListener { loadState ->
            val refreshState = loadState.source.refresh
            when (refreshState) {
                is LoadState.Loading -> {
                   if (movieGenreAdapter.itemCount == 0) {
                       binding.shimmerViewContainer.visibility = View.VISIBLE
                       binding.shimmerViewContainer.startShimmer()
                       binding.rvMovie.visibility = View.GONE
                       binding.layoutEmpty.visibility = View.GONE
                       binding.layoutSearchEmpty.visibility = View.GONE
                   }
                }

                is LoadState.NotLoading -> {

                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE

                    handleEmptyStates(
                        isSearchActive = binding.searchView.query.isNotEmpty(),
                        isListEmpty = movieGenreAdapter.itemCount == 0
                    )

                }

                is LoadState.Error -> {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE

                    if (movieGenreAdapter.itemCount == 0) {
                        handleEmptyStates(isSearchActive = false, isListEmpty = true)
                    }

                }
            }
        }
    }

    private fun initSearchCollector() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.searchMoviesFlow.collect { pagingData ->
                    movieGenreAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                }
            }
        }
    }

    private fun initSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                if (query.isNotEmpty()) {
                    viewModel.searchMovies(query) // Atualiza o fluxo da ViewModel
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotEmpty()) {
                    viewModel.searchMovies(newText) // Atualiza o fluxo da ViewModel
                } else {
                    // Se o campo ficou vazio, limpa a lista e volta para o estado inicial
                    movieGenreAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
                    viewModel.searchMovies("") // Reseta o fluxo na ViewModel também
                    handleEmptyStates(isSearchActive = false, isListEmpty = true)
                }
                return false
            }


        })

        binding.searchView.setOnCloseListener {
            // Se fechou a barra e não tem nada na lista do adapter, volta para o estado inicial
            movieGenreAdapter.submitData(viewLifecycleOwner.lifecycle, PagingData.empty())
            viewModel.searchMovies("")
            handleEmptyStates(isSearchActive = false, isListEmpty = true)
            false
        }

    }

    private fun handleEmptyStates(isSearchActive: Boolean, isListEmpty: Boolean) {
        when {
            // Não tem pesquisa ativa (Estado Inicial) -> Mostra a ilustração de "Pesquise um Filme"
            !isSearchActive -> {
                binding.rvMovie.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.layoutSearchEmpty.visibility = View.GONE
            }

            // Pesquisa ativa mas a lista veio vazia -> Mostra a ilustração de "Nenhum filme encontrado"
            isSearchActive && isListEmpty -> {
                binding.rvMovie.visibility = View.GONE
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutSearchEmpty.visibility = View.VISIBLE
            }

            // Tem filmes! Esconde os avisos e exibe a lista na tela
            else -> {
                binding.rvMovie.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutSearchEmpty.visibility = View.GONE
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}