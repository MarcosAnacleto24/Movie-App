package com.example.movieapp.presenter.main.moviedetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.ItemCommentReviewBinding
import com.example.movieapp.domain.model.MovieReview
import com.example.movieapp.util.circularProgressDrawable
import com.example.movieapp.util.formatCommentDate

class CommentsAdapter(): ListAdapter<MovieReview, CommentsAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<MovieReview>() {
            override fun areItemsTheSame(
                oldItem: MovieReview,
                newItem: MovieReview
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: MovieReview,
                news: MovieReview
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
            ItemCommentReviewBinding.inflate(
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

    inner class MyViewHolder(val binding: ItemCommentReviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(movieReview: MovieReview) {

            Glide.with(binding.root.context)
                .load("https://image.tmdb.org/t/p/w500${movieReview.authorDetails?.avatarPath}") // URL da imagem
                .placeholder(binding.root.context.circularProgressDrawable(3f, 15f)) // Imagem enquanto carrega
                .error(R.drawable.image_profile_error) // Imagem se a URL falhar
                .into(binding.imageUser) // ImageView onde a imagem será exibida


            binding.textNameUser.text = movieReview.authorDetails?.username
            binding.textComment.text = movieReview.content
            binding.textRating.text = movieReview.authorDetails?.rating?.toString() ?: "0"
            binding.textDate.text = formatCommentDate(movieReview.createdAt)



        }
    }



}