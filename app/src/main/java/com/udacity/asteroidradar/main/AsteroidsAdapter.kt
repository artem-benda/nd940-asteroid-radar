package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding

class AsteroidsAdapter(private val clickListener: AsteroidClickListener) : ListAdapter<Asteroid, AsteroidsAdapter.AsteroidViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.fromParent(parent, clickListener)
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        val item = currentList.getOrNull(position) ?: return
        holder.bind(item)
    }

    class AsteroidViewHolder private constructor (private val binding: ListItemAsteroidBinding, private val clickListener: AsteroidClickListener) : RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun fromParent(parent: ViewGroup, clickListener: AsteroidClickListener): AsteroidViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = ListItemAsteroidBinding.inflate(inflater, parent, false)
                return AsteroidViewHolder(binding, clickListener)
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean = oldItem.id == newItem.id
        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean = oldItem == newItem
    }
}

class AsteroidClickListener(private val clickListener: (asteroid: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}
