package com.bucharest.qurio.presentation.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.LastGameCardBinding
import com.bucharest.qurio.presentation.home.state.GameSessionUiModel

class LastGamesAdapter(
    private val onGameClicked: (GameSessionUiModel) -> Unit
) : ListAdapter<GameSessionUiModel, LastGamesAdapter.LastGameViewHolder>(GameSessionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastGameViewHolder {
        val binding = LastGameCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LastGameViewHolder(binding, onGameClicked)
    }

    override fun onBindViewHolder(holder: LastGameViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class LastGameViewHolder(
        private val binding: LastGameCardBinding,
        private val onGameClicked: (GameSessionUiModel) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        private val statsViews by lazy { StatsViews.from(binding.root) }

        fun bind(game: GameSessionUiModel) {
            bindGameInfo(game)
            bindStats(game)
            bindClickListener(game)
        }

        private fun bindGameInfo(game: GameSessionUiModel) {
            binding.gameTitle.text = game.categoryName
        }

        private fun bindStats(game: GameSessionUiModel) {
            with(statsViews) {
                coinsText.text = game.coinsEarned.toString()
                starsText.text = game.starsEarned.toString()
                timeText.text = formatDuration(game.durationSeconds)
                dateText.text = game.playedDate
            }
            updateCoinsColor(game.coinsEarned)
        }

        private fun updateCoinsColor(coinsEarned: Int) {
            val colorRes = if (coinsEarned >= MIN_POSITIVE_COINS) {
                R.color.shade_primary
            } else {
                R.color.red
            }

            statsViews.coinsText.setTextColor(
                ContextCompat.getColor(binding.root.context, colorRes)
            )
        }

        private fun bindClickListener(game: GameSessionUiModel) {
            binding.root.setOnClickListener {
                onGameClicked(game)
            }
        }

        private fun formatDuration(seconds: Int): String {
            val minutes = seconds / SECONDS_PER_MINUTE
            val remainingSeconds = seconds % SECONDS_PER_MINUTE

            return when {
                minutes > MIN_MINUTES_TO_SHOW -> "${minutes}m ${remainingSeconds}sec"
                else -> "${remainingSeconds}sec"
            }
        }

        private data class StatsViews(
            val coinsText: TextView,
            val starsText: TextView,
            val timeText: TextView,
            val dateText: TextView
        ) {
            companion object {
                fun from(root: View): StatsViews {
                    return StatsViews(
                        coinsText = root.findViewById(R.id.coins),
                        starsText = root.findViewById(R.id.stars),
                        timeText = root.findViewById(R.id.time),
                        dateText = root.findViewById(R.id.date)
                    )
                }
            }
        }

        companion object {
            private const val SECONDS_PER_MINUTE = 60
            private const val MIN_MINUTES_TO_SHOW = 0
            private const val MIN_POSITIVE_COINS = 0
        }
    }

    private class GameSessionDiffCallback : DiffUtil.ItemCallback<GameSessionUiModel>() {
        override fun areItemsTheSame(oldItem: GameSessionUiModel, newItem: GameSessionUiModel): Boolean {
            return oldItem.categoryName == newItem.categoryName &&
                   oldItem.playedDate == newItem.playedDate
        }

        override fun areContentsTheSame(oldItem: GameSessionUiModel, newItem: GameSessionUiModel): Boolean {
            return oldItem == newItem
        }
    }
}