package com.wanpaku.pochi.infra.ui.adapter

import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import com.wanpaku.pochi.R

class SingleTitleAdapter(@StringRes private val titleResId: Int) : BaseRecyclerViewAdapter<String, RecyclerView.ViewHolder>() {

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder.itemView as TextView).setText(titleResId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_title, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

}
