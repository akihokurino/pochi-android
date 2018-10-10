package com.wanpaku.pochi.app.sitterdetail

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.wanpaku.pochi.R
import com.wanpaku.pochi.domain.sitter.Image
import com.wanpaku.pochi.infra.ui.adapter.BaseRecyclerViewAdapter
import com.wanpaku.pochi.infra.ui.itemdecoration.MarginItemDecoration
import com.wanpaku.pochi.infra.ui.widget.load

class SitterInteriorImageSwipeView @JvmOverloads constructor(context: Context,
                                                             attrs: AttributeSet? = null,
                                                             defStyleAttr: Int = 0,
                                                             defStyleRes: Int = 0)
    : SitterDetailWithRecyclerViewItem<SitterInteriorImageSwipeView.Adapter>(context, attrs, defStyleAttr, defStyleRes) {

    override fun layoutManager() = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

    override fun itemDecorations() = listOf(
            MarginItemDecoration(spaceBetweenItemWidthPx = resources.getDimensionPixelSize(R.dimen.margin_sitter_interior_image_width),
                    spaceVerticalPx = resources.getDimensionPixelSize(R.dimen.space_16dp))
    )

    override fun titleResId(): Int = R.string.images_of_sitter

    override fun createAdapter() = Adapter()

    fun apply(viewModel: ViewModel) {
        getAdapter().clear()
        getAdapter().addAll(viewModel.interiorImageUrls)
    }

    class ViewModel(interiorImages: List<Image>, context: Context) {

        val interiorImageUrls: List<String> = interiorImages
                .map { it.addSizeUrl(context.resources.getDimensionPixelSize(R.dimen.size_interior_image)) }

    }

    class Adapter : BaseRecyclerViewAdapter<String, Adapter.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_interior_image, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.image.load(get(position))
        }

        class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val image = itemView as ImageView
        }

    }

}