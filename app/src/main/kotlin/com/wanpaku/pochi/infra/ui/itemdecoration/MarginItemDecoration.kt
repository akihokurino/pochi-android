package com.wanpaku.pochi.infra.ui.itemdecoration

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

class MarginItemDecoration(private val spaceBetweenItemWidthPx: Int = 0,
                           private val spaceBetweenItemHeightPx: Int = 0,
                           private val spaceVerticalPx: Int = 0,
                           private val spaceHorizontalPx: Int = 0)
    : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.layoutManager.getPosition(view)
        if (position == 0) {
            outRect.top = spaceBetweenItemHeightPx
            outRect.left = spaceBetweenItemWidthPx
        }
        outRect.top = outRect.top + spaceVerticalPx
        outRect.bottom = spaceBetweenItemHeightPx + spaceVerticalPx
        outRect.right = spaceBetweenItemWidthPx + spaceHorizontalPx
        outRect.left = outRect.left + spaceHorizontalPx
    }

}