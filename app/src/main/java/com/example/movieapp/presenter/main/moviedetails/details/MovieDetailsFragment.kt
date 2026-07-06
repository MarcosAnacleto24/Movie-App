package com.example.movieapp.presenter.main.moviedetails.details

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.DialogDownloadingBinding
import com.example.movieapp.databinding.FragmentMovieDetailsBinding
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presenter.main.moviedetails.Trailers.TrailersFragment
import com.example.movieapp.presenter.main.moviedetails.adapter.CastAdapter
import com.example.movieapp.presenter.main.moviedetails.adapter.ViewPagerAdapter
import com.example.movieapp.presenter.main.moviedetails.comments.CommentsFragment
import com.example.movieapp.presenter.main.moviedetails.similar.SimilarFragment
import com.example.movieapp.util.StateView
import com.example.movieapp.util.ViewPager2ViewHeightAnimator
import com.example.movieapp.util.calculateFileSize
import com.example.movieapp.util.formatDate
import com.example.movieapp.util.initToolbar
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MovieDetailsFragment : Fragment() {
    private var _binding: FragmentMovieDetailsBinding? = null
    private val binding get() = _binding!!

    private lateinit var dialogDownloading: AlertDialog

    private val viewModel: MovieDetailsViewModel by activityViewModels()
    private val safeArgs: MovieDetailsFragmentArgs by navArgs()

    private lateinit var castAdapter: CastAdapter

    private lateinit var movie: Movie


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

        initListeners()

    }

    private fun initListeners() {
        binding.btnDownload.setOnClickListener {
            showDialogDownloading()
        }
    }

    private fun configTabLayout() {

        viewModel.setMovieId(safeArgs.movieId)

        val viewPagerAdapter = ViewPagerAdapter(requireActivity())
        val mViewPage = ViewPager2ViewHeightAnimator()

        mViewPage.viewPager2 = binding.viewPager
        mViewPage.viewPager2?.adapter = viewPagerAdapter

        viewPagerAdapter.addFragment(TrailersFragment(), R.string.title_trailers_tab_layout)
        viewPagerAdapter.addFragment(SimilarFragment(), R.string.title_similar_tab_layout)
        viewPagerAdapter.addFragment(CommentsFragment(), R.string.title_comments_tab_layout)

        binding.viewPager.offscreenPageLimit = viewPagerAdapter.itemCount

        mViewPage.viewPager2?.let { viewPager ->
            TabLayoutMediator(binding.tabLayout, viewPager) { tab, position ->
                tab.text = getString(viewPagerAdapter.getTitle(position))
            }.attach()
        }
    }

    private fun getMovieDetails() {
        viewModel.getMovieDetails(safeArgs.movieId).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Success -> {
                    stateView.data?.let {
                        this.movie = it
                        configData()
                    }

                }

                is StateView.Error -> {

                }
            }
        }
    }

    private fun insertMovie() {
        viewModel.insertMovie(movie).observe(viewLifecycleOwner) { stateView ->
            when (stateView) {
                is StateView.Loading -> {

                }

                is StateView.Success -> {
                    configData()

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

    private fun configData() {

        Glide.with(requireContext())
            .load("https://image.tmdb.org/t/p/w500${movie.backdropPath}")
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

    private fun showDialogDownloading() {
        val dialogBinding = DialogDownloadingBinding.inflate(LayoutInflater.from(requireContext()))
        var progress = 0
        var download = 0.0
        val movieDuration = movie.runtime?.toDouble() ?: 0.0

        val handle = Handler(Looper.getMainLooper())
        val runnable = object : Runnable {
            override fun run() {
                 if (progress < 100) {
                    download += movieDuration / 100.0
                    dialogBinding.textDownloading.text = getString(
                        R.string.text_downloaded_size_dialog_downloading,
                        download.calculateFileSize(),
                        movieDuration.calculateFileSize()
                    )

                    progress++
                    dialogBinding.progressIndicator.progress = progress
                    dialogBinding.textProgress.text = getString(
                        R.string.text_download_progress_dialog_downloading, progress
                    )

                    handle.postDelayed(this, 50)
                } else {
                    insertMovie()
                    dialogDownloading.dismiss()
                 }

            }

        }
        handle.post(runnable)

        val builder = AlertDialog.Builder(requireContext(), R.style.CustomAlertDialog)
        builder.setView(dialogBinding.root)

        dialogBinding.btnHide.setOnClickListener { dialogDownloading.dismiss() }
        dialogBinding.ibCancel.setOnClickListener { dialogDownloading.dismiss() }

        dialogDownloading = builder.create()
        dialogDownloading.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}