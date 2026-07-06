package com.example.movieapp.presenter.main.bottombar.download

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
import androidx.navigation.fragment.findNavController
import com.example.movieapp.MainGraphDirections
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentDownloadBinding
import com.example.movieapp.presenter.main.bottombar.download.adapter.DownloadMovieAdapter
import com.example.movieapp.util.hideKeyboard
import com.ferfalk.simplesearchview.SimpleSearchView
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


        configRecyclerView()

        initObservers()

        initSearchView()
    }

    private fun initObservers() {
        viewModel.movieList.observe(viewLifecycleOwner) { movies ->
            mAdapter.submitList(movies)
        }
    }

    private fun initSearchView() {
        binding.simpleSearchView.setOnQueryTextListener(object : SimpleSearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String): Boolean {
                hideKeyboard()
                if (query.isNotEmpty()) {
                    //getMovies(query)
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
                //getMoviesByGenre()
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
                    findNavController().navigate(action)
                }
            },
            deleteOnClick = { movieId ->

            }
        )

        with(binding.rvMovies) {
            setHasFixedSize(true)
            adapter = mAdapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}