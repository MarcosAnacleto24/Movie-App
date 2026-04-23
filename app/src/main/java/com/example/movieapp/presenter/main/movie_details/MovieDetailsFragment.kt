package com.example.movieapp.presenter.main.movie_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.FragmentMovieDetailsBinding
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.util.StateView
import com.example.movieapp.util.initToolbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by viewModels()
    private val safeArgs: MovieDetailsFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)

        getMovieDetails()

    }

    private fun getMovieDetails() {
        viewModel.getMovieDetails(safeArgs.movieId).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Success -> {
                    stateView.data?.let {
                        configData(it)
                    }

                }

                is StateView.Error -> {

                }
            }
        }
    }

    private fun configData(movie: Movie) {

        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w500${movie.posterPath}")
            .placeholder(R.drawable.placeholder_image) // Imagem enquanto carrega
            .error(R.drawable.error_image) // Imagem se a URL falhar
            .into(binding.imageMovie)

        binding.txtTitle.text = movie.title

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}