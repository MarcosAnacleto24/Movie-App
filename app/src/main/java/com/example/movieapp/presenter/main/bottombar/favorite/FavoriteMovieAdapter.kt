package com.example.movieapp.presenter.main.bottombar.favorite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieFavoriteItemBinding
import com.example.movieapp.domain.model.favorite.FavoriteMovie
import com.example.movieapp.util.circularProgressDrawable
import java.util.Locale

class FavoriteMovieAdapter(
    private val context: Context,
    private val onClick: (id: Int?) -> Unit
) : ListAdapter<FavoriteMovie, FavoriteMovieAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<FavoriteMovie>() {
            override fun areItemsTheSame(oldItem: FavoriteMovie, newItem: FavoriteMovie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FavoriteMovie, newItem: FavoriteMovie): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            MovieFavoriteItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class MyViewHolder(private val binding: MovieFavoriteItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoriteMovie: FavoriteMovie) {
            Glide.with(context)
                .load("https://image.tmdb.org/t/p/w500${favoriteMovie.posterPath}")
                .placeholder(context.circularProgressDrawable())
                .error(R.drawable.error_image)
                .into(binding.movieImg)

            // Exibe a nota formatada com 1 casa decimal (ex: 9.5)
            val rating = favoriteMovie.voteAverage ?: 0.0f
            binding.textVoteAverage.text = String.format(Locale.getDefault(), "%.1f", rating)

            binding.root.setOnClickListener {
                onClick(favoriteMovie.id)
            }
        }
    }
}