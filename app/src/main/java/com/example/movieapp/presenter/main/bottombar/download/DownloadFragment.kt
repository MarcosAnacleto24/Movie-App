package com.example.movieapp.presenter.main.bottombar.download

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
import com.bumptech.glide.Glide
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.R
import com.example.movieapp.databinding.BottomSheetDeleteMovieBinding
import com.example.movieapp.databinding.FragmentDownloadBinding
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presenter.main.bottombar.download.adapter.DownloadMovieAdapter
import com.example.movieapp.util.NavAnimationType
import com.example.movieapp.util.animateNavigation
import com.example.movieapp.util.calculateFileSize
import com.example.movieapp.util.calculateMovieTime
import com.example.movieapp.util.hideKeyboard
import com.example.movieapp.util.initToolbar
import com.ferfalk.simplesearchview.SimpleSearchView
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadFragment : Fragment() {
    private var _binding: FragmentDownloadBinding? = null
    private val binding get() = _binding!!

    private lateinit var mAdapter: DownloadMovieAdapter

    private val viewModel: DownloadViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDownloadBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        initToolbar(binding.toolbar, showIconNavigation = false)


        configRecyclerView()

        initObservers()

        initListeners()

    }

    private fun initObservers() {
        viewModel.movieList.observe(viewLifecycleOwner) { movies ->
           if (!binding.simpleSearchView.isSearchOpen)
                mAdapter.submitList(movies)
                handleEmptyStates(false, movies.isEmpty())
        }

        viewModel.movieSearchList.observe(viewLifecycleOwner) { movies ->
            if (binding.simpleSearchView.isSearchOpen) {
                mAdapter.submitList(movies)
                handleEmptyStates(true, movies.isEmpty())
            }
        }
    }

    private fun initListeners() {
        initSearchView()

        onBackPressed()
    }

    private fun initSearchView() {
        binding.simpleSearchView.setOnQueryTextListener(object :
            SimpleSearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()

                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNotBlank()) {
                    viewModel.searchMovie(newText)
                } else {
                    val currentMovies = viewModel.movieList.value ?: mutableListOf()
                    mAdapter.submitList(currentMovies)
                    handleEmptyStates(false, currentMovies.isEmpty())

                }
                return true
            }

            override fun onQueryTextCleared(): Boolean {
                return false
            }


        })

        binding.simpleSearchView.setOnSearchViewListener(object :
            SimpleSearchView.SearchViewListener {
            override fun onSearchViewClosed() {
                val currentMovies = viewModel.movieList.value ?: mutableListOf()
                mAdapter.submitList(currentMovies)
                handleEmptyStates(isSearchActive = false, isListEmpty = currentMovies.isEmpty())
                hideKeyboard()
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

    private fun configRecyclerView() {

        mAdapter = DownloadMovieAdapter(
            requireContext(),
            detailsOnClick = { movieId ->
                movieId?.let {
                    val action = MainGraphDirections.actionGlobalMovieDetailsFragment(movieId)
                    findNavController().animateNavigation(action)
                }
            },
            deleteOnClick = { movie ->
                showBottomSheetDeleteMovie(movie)
            }
        )

        with(binding.rvMovies) {
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    private fun showBottomSheetDeleteMovie(movie: Movie) {
        val bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.BottomSheetDialog)
        val bottomSheetBinding = BottomSheetDeleteMovieBinding.inflate(layoutInflater, null, false)

        // Carregando a imagem com glide
        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w200${movie.posterPath}") // URL da imagem
            .placeholder(R.drawable.placeholder_image) // Imagem enquanto carrega
            .error(R.drawable.error_image) // Imagem se a URL falhar
            .into(bottomSheetBinding.ivMovie)

        bottomSheetBinding.textMovie.text = movie.title
        bottomSheetBinding.textDuration.text = movie.runtime?.calculateMovieTime()
        bottomSheetBinding.textSize.text = movie.runtime?.toDouble()?.calculateFileSize()

        bottomSheetBinding.btnCancel.setOnClickListener {
            bottomSheetDialog.dismiss()
        }

        bottomSheetBinding.btnConfirm.setOnClickListener {
            bottomSheetDialog.dismiss()

            movie.id?.let { movieId ->
                viewModel.deleteMovies(movieId)
            }
        }

        bottomSheetDialog.setContentView(bottomSheetBinding.root)
        bottomSheetDialog.show()
    }

    private fun handleEmptyStates(isSearchActive: Boolean, isListEmpty: Boolean) {
        when {
            // Pesquisa ativa e nenhum filme encontrado
            isSearchActive && isListEmpty -> {
                binding.rvMovies.visibility = View.GONE
                binding.layoutEmpty.visibility = View.VISIBLE          // Imagem de busca vazia
                binding.layoutEmptyListMovies.visibility = View.GONE
            }
            // Fora da pesquisa e o banco de dados está zerado
            !isSearchActive && isListEmpty -> {
                binding.rvMovies.visibility = View.GONE
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutEmptyListMovies.visibility = View.VISIBLE // Imagem de banco vazio
            }
            // Tem dados para mostrar! Esconde tudo e mostra o RecyclerView
            else -> {
                binding.rvMovies.visibility = View.VISIBLE
                binding.layoutEmpty.visibility = View.GONE
                binding.layoutEmptyListMovies.visibility = View.GONE
            }
        }
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