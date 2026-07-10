package com.example.movieapp.presenter.main.bottombar.download.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieDownloadItemBinding
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.util.calculateFileSize
import com.example.movieapp.util.calculateMovieTime
import com.example.movieapp.util.circularProgressDrawable

class DownloadMovieAdapter(
    private val context: Context,
    private val detailsOnClick: (id: Int?) -> Unit,
    private val deleteOnClick: (movie: Movie) -> Unit
): ListAdapter<Movie, DownloadMovieAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Movie>() {
            override fun areItemsTheSame(
                oldItem: Movie,
                newItem: Movie
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Movie,
                news: Movie
            ): Boolean {
                return oldItem == news
            }

        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        return MyViewHolder(
            MovieDownloadItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val article = getItem(position)
        holder.bind(article)
    }

    inner class MyViewHolder(val binding: MovieDownloadItemBinding ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {

            // Carregando a imagem com glide
            Glide.with(context)
                 .load("https://image.tmdb.org/t/p/w200${movie.posterPath}") // URL da imagem
                 .placeholder(context.circularProgressDrawable()) // Imagem enquanto carrega
                 .error(R.drawable.error_image) // Imagem se a URL falhar
                 .into(binding.ivMovie)

            binding.textMovie.text = movie.title
            binding.textDuration.text = movie.runtime?.calculateMovieTime()
            binding.textSize.text = movie.runtime?.toDouble()?.calculateFileSize()

            binding.ivMovie.setOnClickListener {
                detailsOnClick(movie.id)
            }

            binding.ibDelete.setOnClickListener {
                deleteOnClick(movie)
            }



        }
    }



}