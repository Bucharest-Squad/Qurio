package com.bucharest.qurio.presentation.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.LastGameCardBinding

class LastGamesAdapter(
    private val onGameClicked: (GameSessionState) -> Unit
) : RecyclerView.Adapter<LastGamesAdapter.LastGameViewHolder>() {

    private var games: List<GameSessionState> = emptyList()

    fun submitList(gamesList: List<GameSessionState>) {
        this.games = gamesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LastGameViewHolder {
        val binding = LastGameCardBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return LastGameViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LastGameViewHolder, position: Int) {
        holder.bind(games[position])
    }

    override fun getItemCount(): Int = games.size

    inner class LastGameViewHolder(
        private val binding: LastGameCardBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        // Manually find views from merged includes
        private val coinsText: TextView = binding.root.findViewById(R.id.coins)
        private val starsText: TextView = binding.root.findViewById(R.id.stars)
        private val timeText: TextView = binding.root.findViewById(R.id.time)
        private val dateText: TextView = binding.root.findViewById(R.id.date)

        fun bind(game: GameSessionState) {
            // Set game title
            binding.gameTitle.text = game.categoryName
            
            // Set stats using findViewById (since merge includes don't expose in binding)
            coinsText.text = game.coinsEarned.toString()
            starsText.text = game.starsEarned.toString()
            timeText.text = formatDuration(game.durationSeconds)
            dateText.text = game.playedDate
            
            // Set coins color based on value
            if (game.coinsEarned >= 0) {
                coinsText.setTextColor(
                    androidx.core.content.ContextCompat.getColor(
                        binding.root.context,
                        R.color.shade_primary
                    )
                )
            } else {
                // Keep original color (red) for negative values
                coinsText.setTextColor(
                    androidx.core.content.ContextCompat.getColor(
                        binding.root.context,
                        R.color.red
                    )
                )
            }
            
            binding.root.setOnClickListener {
                onGameClicked(game)
            }
        }

        private fun formatDuration(seconds: Int): String {
            val minutes = seconds / 60
            val secs = seconds % 60
            return if (minutes > 0) {
                "${minutes}m ${secs}sec"
            } else {
                "${secs}sec"
            }
        }
    }
}

