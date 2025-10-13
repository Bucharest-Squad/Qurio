package com.bucharest.qurio.presentation.home.components.carousel

import android.annotation.SuppressLint
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.presentation.home.components.GameCard
import com.bucharest.qurio.presentation.home.state.CategoryUiModel

class CategoryCarouselAdapter(
    private val onCategoryClicked: (id: Int) -> Unit
) : RecyclerView.Adapter<CategoryCarouselAdapter.CarouselViewHolder>() {

    private var categories: List<CategoryUiModel> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(categoryList: List<CategoryUiModel>) {
        this.categories = categoryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        val gameCard = GameCard(parent.context)
        gameCard.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return CarouselViewHolder(gameCard, onCategoryClicked)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int = categories.size

    class CarouselViewHolder(
        private val gameCard: GameCard,
        private val onCategoryClicked: (id: Int) -> Unit
    ) : RecyclerView.ViewHolder(gameCard) {

        fun bind(category: CategoryUiModel) {
            gameCard.setState(
                title = category.title,
                imageRes = category.imageRes,
                startColor = category.startColor,
                endColor = category.endColor
            )
            gameCard.setOnClickListener { 
                onCategoryClicked(category.id) 
            }
        }
    }

}
