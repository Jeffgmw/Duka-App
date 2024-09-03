package com.example.somalearn.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.somalearn.data.local.entities.CachedBook

class DiffUtilCallback(
    private val oldList: List<CachedBook>,
    private val newList: List<CachedBook>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].book.trackId == newList[newItemPosition].book.trackId
    }
}
