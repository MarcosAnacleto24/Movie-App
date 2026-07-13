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
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieGenreBinding
import com.example.movieapp.presenter.main.moviegenre.adapter.MovieGenreLoadStatePagingAdapter
import com.example.movieapp.presenter.main.moviegenre.adapter.MovieGenreAdapter
import com.example.movieapp.util.hideKeyboard
import com.example.movieapp.util.initToolbar
import com.example.movieapp.util.showSnackBarString
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

        initMoviesCollector()

        initSearchView()


    }

    private fun initMoviesCollector() {
        viewModel.setGenreId(safeArgs.genreId)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                // Verificamos se a barra de busca está aberta no momento do ciclo de vida
                if (binding.simpleSearchView.isSearchOpen) {
                    // Se a busca estiver aberta ao voltar da tela, mantém o foco no fluxo de busca
                    viewModel.searchMoviesFlow.collect { pagingData ->
                        movieGenreAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                    }
                } else {
                    // Se a busca estiver fechada, consome os dados normais por gênero
                    viewModel.moviesByGenreFlow.collect { pagingData ->
                        movieGenreAdapter.submitData(viewLifecycleOwner.lifecycle, pagingData)
                    }
                }
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

        // Cria o seu Grid indicando a quantidade de colunas (exemplo: 2 ou 3 colunas)
        val gridLayoutManager = GridLayoutManager(requireContext(), 2)

        //  Diz para o Grid ocupar todas as colunas se o item for o rodapé de loading
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                // Se o tipo do item for o do rodapé de carregamento, ele ocupa o total de colunas (2)
                // Caso contrário, ocupa apenas 1 espaço (comportamento padrão do item de filme)
                return if (position == movieGenreAdapter.itemCount && movieGenreAdapter.itemCount > 0) {
                    gridLayoutManager.spanCount // Ocupa a largura total (todas as colunas)
                } else {
                    1 // Item comum ocupa apenas uma coluna
                }
            }
        }

        with(binding.rvMovieGenre) {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            adapter = movieGenreAdapter.withLoadStateFooter(
                footer = MovieGenreLoadStatePagingAdapter()
            )
        }

        initAdapterLoadStateListener()

    }

    private fun initAdapterLoadStateListener() {
        movieGenreAdapter.addLoadStateListener { loadStates ->
            when (val refreshState = loadStates.refresh) {
                is LoadState.Loading -> {

                   if (movieGenreAdapter.itemCount == 0) {
                       binding.rvMovieGenre.visibility = View.GONE
                       binding.shimmerViewContainer.visibility = View.VISIBLE
                       binding.shimmerViewContainer.startShimmer()
                   }
                }

                is LoadState.NotLoading -> {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE
                    binding.rvMovieGenre.visibility = View.VISIBLE
                }

                is LoadState.Error -> {
                    binding.shimmerViewContainer.stopShimmer()
                    binding.shimmerViewContainer.visibility = View.GONE

                    if (movieGenreAdapter.itemCount == 0) {
                        binding.rvMovieGenre.visibility = View.GONE
                    }

                    val error = refreshState.error
                    val message = error.localizedMessage
                    showSnackBarString(
                        message = getString(
                            R.string.error_loading_movies,
                            message ?: getString(R.string.unknown_error)
                        )
                    )


                }
            }
        }
    }

    private fun initSearchView() {
        binding.simpleSearchView.setOnQueryTextListener(object :
            SimpleSearchView.OnQueryTextListener {

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

        binding.simpleSearchView.setOnSearchViewListener(object :
            SimpleSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                viewModel.searchMovies("") // Limpa o fluxo de busca quando a barra de busca é fechada
                initMoviesCollector()
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

       viewModel.searchMovies(query)
       initMoviesCollector()

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}