package com.bucharest.qurio.presentation.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.databinding.ItemCharacterCardBinding
import com.bucharest.qurio.presentation.character_dialog.CharacterUiModel
import com.bucharest.qurio.presentation.component.CharactersDialog

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
            characterImage.setImageResource(characterUiModel.imageRes)
            selectedIcon.visibility = if(selectedCharId==characterUiModel.id) VISIBLE else GONE
            lockedIcon.visibility = if (characterUiModel.isOwned)  GONE else VISIBLE
            lockedOverlay.visibility = if (characterUiModel.isOwned) GONE else VISIBLE
            coinIcons.visibility = if (characterUiModel.isOwned) GONE else VISIBLE
            pointsCount.text = characterUiModel.characterPrice
            pointsCount.visibility = if (characterUiModel.isOwned) GONE else VISIBLE

            root.setOnClickListener {
                if (characterUiModel.id == selectedCharId && characterUiModel.isOwned) {
                    //CharactersDialog(){}.show()

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

