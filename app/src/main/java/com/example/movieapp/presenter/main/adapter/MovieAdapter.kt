package com.example.movieapp.presenter.main.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.MovieItemBinding
import com.example.movieapp.domain.model.Movie

class MovieAdapter(
    private val context: Context
): ListAdapter<Movie, MovieAdapter.MyViewHolder>(DIFF_CALLBACK) {

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
            MovieItemBinding.inflate(
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

    inner class MyViewHolder(val binding: MovieItemBinding ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {

            // Carregando a imagem com glide
            Glide.with(context)
                 .load("https://image.tmdb.org/t/p/w500${movie.posterPath}") // URL da imagem
                 .placeholder(R.drawable.placeholder_image) // Imagem enquanto carrega
                 .error(R.drawable.error_image) // Imagem se a URL falhar
                 .into(binding.movieImg)


//            // Configura o clique para a tela de Detalhes
//            binding.root.setOnClickListener {
//                onClick(movie)
//            }





        }
    }



}