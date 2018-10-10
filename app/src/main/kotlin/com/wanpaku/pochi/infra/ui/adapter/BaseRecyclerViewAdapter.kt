package com.wanpaku.pochi.infra.ui.adapter

import android.support.v7.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<Item, ViewHolder : RecyclerView.ViewHolder> : RecyclerView.Adapter<ViewHolder>() {

    private val items = mutableListOf<Item>()

    override fun getItemCount() = items.count()

    fun add(item: Item) {
        items.add(item)
        notifyItemInserted(itemCount - 1)
    }

    fun insertAll(insertItems: List<Item>, position: Int) {
        items.addAll(position, insertItems)
        notifyItemRangeInserted(position, insertItems.size)
    }

    fun addAll(addItems: List<Item>) {
        val start = itemCount + 1
        items.addAll(addItems)
        notifyItemRangeInserted(start, addItems.size)
    }

    fun clear() {
        val itemCount = itemCount
        items.clear()
        notifyItemRangeRemoved(0, itemCount)
    }

    fun get(index: Int): Item = items[index]

}