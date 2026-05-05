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
import com.example.movieapp.presenter.main.movie_details.adapter.CastAdapter
import com.example.movieapp.presenter.main.movie_details.adapter.ViewPagerAdapter
import com.example.movieapp.util.StateView
import com.example.movieapp.util.formatDate
import com.example.movieapp.util.initToolbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MovieDetailsViewModel by viewModels()
    private val safeArgs: MovieDetailsFragmentArgs by navArgs()

    private lateinit var castAdapter: CastAdapter


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

        initRecyclerCredits()

        configTabLayout()

    }

    private fun configTabLayout() {
        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = viewPagerAdapter

            viewPagerAdapter.addFragment(TrailersFragment(), R.string.title_trailers_tab_layout)
            viewPagerAdapter.addFragment(SimilarFragment(), R.string.title_similar_tab_layout)
            viewPagerAdapter.addFragment(CommentsFragment(), R.string.title_comments_tab_layout)

        binding.viewPager.offscreenPageLimit = viewPagerAdapter.itemCount

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
                tab.text = getString(viewPagerAdapter.getTitle(position))
            }.attach()
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

    private fun initRecyclerCredits() {

        castAdapter = CastAdapter()

        with(binding.recyclerCast) {
            setHasFixedSize(true)
            adapter = castAdapter
        }
    }

    private fun getMovieCredits() {
        viewModel.getMovieCredits(safeArgs.movieId).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Success -> {
                    stateView.data?.let {
                        castAdapter.submitList(it.cast)
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

        binding.textVoteAverage.text = String.format(Locale.getDefault(), "%.1f", movie.voteAverage)

        binding.textProductionCountry.text = movie.productionCompanies?.firstOrNull()?.name ?: ""

        binding.textYear.text = movie.releaseDate?.formatDate()

        val genres = movie.genres?.joinToString(", ") { it.name.toString() } ?: ""

        binding.textGenres.text = getString(R.string.text_genres_movie_detail_fragment, genres)

        binding.textDescription.text = movie.overview

        getMovieCredits()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}