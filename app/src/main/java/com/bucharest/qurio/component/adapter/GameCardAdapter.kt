package com.bucharest.qurio.component.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.component.GameCard

class GameCardAdapter
    (
     private val onGameCardClicked: (id:Int) -> Unit,
) : RecyclerView.Adapter<GameCardAdapter.GameCardAdapterViewHolder>() {

    private var categoryList: List<CategoryUiModel> = emptyList()
    fun submitList(categoryList: List<CategoryUiModel>) {
        this.categoryList=categoryList
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(
        parent: android.view.ViewGroup, viewType: Int
    ): GameCardAdapterViewHolder {
        return GameCardAdapterViewHolder(GameCard(parent.context))
    }

    override fun onBindViewHolder(holder: GameCardAdapterViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int =categoryList.size

    inner class GameCardAdapterViewHolder(val gameCard: GameCard) : RecyclerView.ViewHolder(gameCard) {
        fun bind(categoryUiModel: CategoryUiModel) {
            gameCard.setState(
                title = categoryUiModel.title,
                imageRes = categoryUiModel.imageRes,
                startColor = categoryUiModel.startColor,
                endColor = categoryUiModel.endColor
            )
            gameCard.setOnClickListener { onGameCardClicked(categoryUiModel.id) }
        }

    }
}

data class CategoryUiModel(
    val id:Int,val title: String, val imageRes: Int, val startColor: Int, val endColor: Int
)