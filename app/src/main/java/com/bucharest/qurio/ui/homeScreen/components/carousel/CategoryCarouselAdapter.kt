package com.bucharest.qurio.ui.homeScreen.components.carousel

import android.annotation.SuppressLint
import android.util.Log
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bucharest.qurio.component.GameCard
import com.bucharest.qurio.component.adapter.CategoryUiModel

class CategoryCarouselAdapter(
    private val onCategoryClicked: (id: Int) -> Unit
) : RecyclerView.Adapter<CategoryCarouselAdapter.CarouselViewHolder>() {

    private var categories: List<CategoryUiModel> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(categoryList: List<CategoryUiModel>) {
        Log.d(TAG, "submitList called with ${categoryList.size} categories")
        this.categories = categoryList
        notifyDataSetChanged()
        Log.d(TAG, "Adapter now has ${categories.size} items, itemCount=${itemCount}")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarouselViewHolder {
        Log.d(TAG, "onCreateViewHolder called")
        val gameCard = GameCard(parent.context)
        gameCard.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        return CarouselViewHolder(gameCard)
    }

    override fun onBindViewHolder(holder: CarouselViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder called for position=$position, category=${categories[position].title}")
        holder.bind(categories[position])
    }

    override fun getItemCount(): Int {
        val count = categories.size
        Log.d(TAG, "getItemCount() returning $count")
        return count
    }

    inner class CarouselViewHolder(private val gameCard: GameCard) : 
        RecyclerView.ViewHolder(gameCard) {

        fun bind(category: CategoryUiModel) {
            Log.d(TAG, "Binding category: ${category.title}")
            gameCard.setState(
                title = category.title,
                imageRes = category.imageRes,
                startColor = category.startColor,
                endColor = category.endColor
            )
            gameCard.setOnClickListener { 
                Log.d(TAG, "Category clicked: ${category.title} (id=${category.id})")
                onCategoryClicked(category.id) 
            }
        }
    }

    companion object {
        private const val TAG = "CategoryCarouselAdapter"
    }
}

