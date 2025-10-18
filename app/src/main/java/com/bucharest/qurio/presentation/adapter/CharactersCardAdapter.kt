package com.bucharest.qurio.presentation.adapter

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.databinding.ItemCharacterCardBinding
import com.bucharest.qurio.presentation.character_dialog.CharacterUiModel

class CharactersCardAdapter
    (
    private var selectedCharId:Int,
    private val onCharacterCardClicked: (id: Int) -> Unit,
    private val onCharacterCardDoubleClicked: (characterUiModel: CharacterUiModel) -> Unit,
) : RecyclerView.Adapter<CharactersCardAdapter.CharactersCardAdapterViewHolder>() {

    private var charactersList: List<CharacterUiModel> = emptyList()
    fun submitList(categoryList: List<CharacterUiModel>) {
        this.charactersList = categoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): CharactersCardAdapterViewHolder {
        return CharactersCardAdapterViewHolder(
            ItemCharacterCardBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: CharactersCardAdapterViewHolder, position: Int) {
        holder.bind(charactersList[position])
    }

    override fun getItemCount(): Int = charactersList.size

    inner class CharactersCardAdapterViewHolder(val binding: ItemCharacterCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(characterUiModel: CharacterUiModel) = with(binding) {
            characterName.text = characterUiModel.characterName
            characterImage.setImageResource(characterUiModel.imageRes.first)
            selectedIcon.visibility = if(selectedCharId==characterUiModel.id) VISIBLE else GONE
            lockedIcon.visibility = if (characterUiModel.isOwned)  GONE else VISIBLE
            lockedOverlay.visibility = if (characterUiModel.isOwned) GONE else VISIBLE
            coinIcons.visibility = if (characterUiModel.isOwned) GONE else VISIBLE
            pointsCount.text = characterUiModel.characterPrice
            pointsCount.visibility = if (characterUiModel.isOwned) GONE else VISIBLE
            
            if (!characterUiModel.isOwned) {
                root.alpha = if (characterUiModel.canAfford) 1.0f else 0.6f
            }

            root.setOnClickListener {
                if (characterUiModel.id == selectedCharId && characterUiModel.isOwned) {
                    onCharacterCardDoubleClicked(characterUiModel)
                }
                if(characterUiModel.isOwned) {
                    selectedCharId = characterUiModel.id
                    onCharacterCardClicked(characterUiModel.id)
                    notifyDataSetChanged()
                }else{
                    onCharacterCardDoubleClicked(characterUiModel)
                }
            }

        }
    }

}

