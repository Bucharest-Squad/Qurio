package com.bucharest.qurio.component.adapter

import androidx.recyclerview.widget.DiffUtil

class GameCardDiffUtil(
    private val oldList: List<CategoryUiModel>, private val newList: List<CategoryUiModel>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(
        oldItemPosition: Int, newItemPosition: Int
    ): Boolean = oldList[oldItemPosition].id == newList[newItemPosition].id

    override fun areContentsTheSame(
        oldItemPosition: Int, newItemPosition: Int
    ): Boolean = oldList[oldItemPosition] == newList[newItemPosition]
}