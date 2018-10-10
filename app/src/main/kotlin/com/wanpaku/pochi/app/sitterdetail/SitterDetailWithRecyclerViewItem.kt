package com.wanpaku.pochi.app.sitterdetail

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.TextView
import com.wanpaku.pochi.R
import com.wanpaku.pochi.infra.ui.adapter.BaseRecyclerViewAdapter

abstract class SitterDetailWithRecyclerViewItem<out T : BaseRecyclerViewAdapter<*, *>> @JvmOverloads constructor(context: Context,
                                                                                                                 attrs: AttributeSet? = null,
                                                                                                                 defStyleAttr: Int = 0,
                                                                                                                 defStyleRes: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val recyclerView: RecyclerView

    abstract fun layoutManager(): RecyclerView.LayoutManager

    abstract fun itemDecorations(): List<RecyclerView.ItemDecoration>

    abstract fun titleResId(): Int

    abstract fun createAdapter(): T

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_sitter_house_about, this)
        recyclerView = view.findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(false)
            layoutManager = layoutManager()
            itemDecorations().forEach { addItemDecoration(it) }
            adapter = createAdapter()
            adapter = createAdapter()
        }
        view.findViewById<TextView>(R.id.title).apply {
            setText(titleResId())
        }
    }

    protected fun getAdapter(): T = recyclerView.adapter as T

}
