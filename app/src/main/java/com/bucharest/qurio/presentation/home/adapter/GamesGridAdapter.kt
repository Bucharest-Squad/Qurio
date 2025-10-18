package com.bucharest.qurio.presentation.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.audio.AudioManager
import com.bucharest.qurio.databinding.ItemSmallGameCardBinding
import com.bucharest.qurio.presentation.home.components.SmallGameCard
import com.bucharest.qurio.presentation.home.state.CategoryUiModel
import javax.inject.Inject

class GamesGridAdapter(
    private val onCategoryClicked: (id: Int) -> Unit,
    private val audioManager: AudioManager
) : ListAdapter<CategoryUiModel, GamesGridAdapter.GameViewHolder>(CategoryDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = ItemSmallGameCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GameViewHolder(binding, onCategoryClicked, audioManager)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class GameViewHolder(
        private val binding: ItemSmallGameCardBinding,
        private val onCategoryClicked: (id: Int) -> Unit,
        private val audioManager: AudioManager
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(category: CategoryUiModel) {
            binding.categoryTitle.text = category.title
            binding.categoryThumbnail.setImageResource(category.imageRes)
            
            // Set gradient colors
            val gradient = android.graphics.drawable.GradientDrawable(
                android.graphics.drawable.GradientDrawable.Orientation.TOP_BOTTOM,
                intArrayOf(category.startColor, category.endColor)
            ).apply {
                cornerRadii = floatArrayOf(
                    0f, 0f, 0f, 0f,
                    8f.dp, 8f.dp, 8f.dp, 8f.dp
                )
            }
            binding.gradientOverlay.background = gradient
            
            // Set click listeners
            binding.playButton.setOnClickListener {
                audioManager.playButtonPress()
                onCategoryClicked(category.id)
            }
            
            binding.root.setOnClickListener {
                audioManager.playButtonPress()
                onCategoryClicked(category.id)
            }
        }

        private val Float.dp: Float get() = this * binding.root.context.resources.displayMetrics.density
    }

    private class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryUiModel>() {
        override fun areItemsTheSame(oldItem: CategoryUiModel, newItem: CategoryUiModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: CategoryUiModel, newItem: CategoryUiModel): Boolean {
            return oldItem == newItem
        }
    }
}
