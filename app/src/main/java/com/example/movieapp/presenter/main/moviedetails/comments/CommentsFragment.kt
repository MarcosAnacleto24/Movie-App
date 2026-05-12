package com.example.movieapp.presenter.main.moviedetails.comments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.movieapp.databinding.FragmentCommentsBinding
import com.example.movieapp.presenter.main.moviedetails.adapter.CommentsAdapter
import com.example.movieapp.presenter.main.moviedetails.details.MovieDetailsViewModel
import com.example.movieapp.util.StateView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CommentsFragment : Fragment() {
    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!

    private lateinit var commentsAdapter: CommentsAdapter

    private val viewModel: CommentsViewModel by activityViewModels()

    private val movieDetailsViewModel: MovieDetailsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCommentsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        initObservers()

    }

    private fun initObservers() {
        movieDetailsViewModel.movieId.observe(viewLifecycleOwner) { movieId ->
            if (movieId > 0) {
                getMovieReviews(movieId)
            }
        }
    }

    private fun initRecycler() {
        commentsAdapter = CommentsAdapter()

        with(binding.recyclerComments) {
            setHasFixedSize(true)
            adapter = commentsAdapter
        }

    }

    private fun getMovieReviews(movieId: Int) {
        viewModel.getMovieReviews(movieId).observe(viewLifecycleOwner) { stateView ->
            when(stateView) {
                is StateView.Loading -> {

                }
                is StateView.Success -> {
                    val reviews = stateView.data?.results
                    commentsAdapter.submitList(reviews)
                }
                is StateView.Error -> {

                }
            }
        }

    }






    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}