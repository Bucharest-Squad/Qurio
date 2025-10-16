package com.bucharest.qurio.presentation.home.adapter

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.presentation.home.components.GameCard
import com.bucharest.qurio.presentation.home.state.CategoryUiModel

class GameCardAdapter(
    private val onGameCardClicked: (id: Int) -> Unit,
) : RecyclerView.Adapter<GameCardAdapter.GameCardAdapterViewHolder>() {

    private var categoryList: List<CategoryUiModel> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(categoryList: List<CategoryUiModel>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GameCardAdapterViewHolder {
        return GameCardAdapterViewHolder(GameCard(parent.context), onGameCardClicked)
    }

    override fun onBindViewHolder(holder: GameCardAdapterViewHolder, position: Int) {
        holder.bind(categoryList[position])
    }

    override fun getItemCount(): Int = categoryList.size

    class GameCardAdapterViewHolder(
        private val gameCard: GameCard,
        private val onGameCardClicked: (id: Int) -> Unit
    ) : RecyclerView.ViewHolder(gameCard) {
        
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