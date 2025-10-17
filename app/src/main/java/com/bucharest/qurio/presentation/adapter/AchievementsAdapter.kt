package com.bucharest.qurio.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.R
import com.bucharest.qurio.databinding.AchievementCardBinding
import com.bucharest.qurio.presentation.achievemetns_dialog.AchievementUImodel

class AchievementsAdapter
    (
    private val onCharacterCardClicked: (achievementUImodel:AchievementUImodel) -> Unit,
) : RecyclerView.Adapter<AchievementsAdapter.AchievementsCardViewHolder>() {

    private var charactersList: List<AchievementUImodel> = emptyList()
    fun submitList(categoryList: List<AchievementUImodel>) {
        this.charactersList = categoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): AchievementsCardViewHolder {
        return AchievementsCardViewHolder(
            AchievementCardBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AchievementsCardViewHolder, position: Int) {
        holder.bind(charactersList[position])
    }

    override fun getItemCount(): Int = charactersList.size

    inner class AchievementsCardViewHolder(val binding: AchievementCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(achievementUImodel: AchievementUImodel) = with(binding) {
            characterName.text = achievementUImodel.title
            characterName.setTextColor(
                ContextCompat.getColor(
                    characterName.context,
                    if (achievementUImodel.unlocked)
                        R.color.shade_primary
                    else
                        R.color.shade_tertiary
                )
            )
            characterImage.setImageResource(
                if (achievementUImodel.unlocked)
                    achievementUImodel.imageResFilledAndOutlined.first
                        else achievementUImodel.imageResFilledAndOutlined.second
            )

            root.setOnClickListener {
                onCharacterCardClicked(achievementUImodel)
            }

        }
    }

}

