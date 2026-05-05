package com.example.movieapp.presenter.main.movie_details.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.movieapp.R
import com.example.movieapp.databinding.CastItemBinding
import com.example.movieapp.domain.model.Person

class CastAdapter(): ListAdapter<Person, CastAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Person>() {
            override fun areItemsTheSame(
                oldItem: Person,
                newItem: Person
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: Person,
                news: Person
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
            CastItemBinding.inflate(
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

    inner class MyViewHolder(val binding: CastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Person) {

            // Carregando a imagem com glide
            Glide.with(binding.root.context)
                 .load("https://image.tmdb.org/t/p/w500${person.profilePath}") // URL da imagem
                 .placeholder(R.drawable.placeholder_image) // Imagem enquanto carrega
                 .error(R.drawable.error_image) // Imagem se a URL falhar
                 .into(binding.personImage) // ImageView onde a imagem será exibida


                binding.personName.text = person.name


        }
    }



}