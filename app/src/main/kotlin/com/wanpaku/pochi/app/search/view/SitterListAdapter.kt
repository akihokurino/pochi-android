package com.wanpaku.pochi.app.search.view

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.request.RequestOptions
import com.wanpaku.pochi.R
import com.wanpaku.pochi.domain.dog.Dog
import com.wanpaku.pochi.domain.sitter.Sitter
import com.wanpaku.pochi.infra.ui.adapter.BaseRecyclerViewAdapter
import com.wanpaku.pochi.infra.ui.widget.load


class SitterListAdapter(private val sitterClickedListener: (sitter: Sitter) -> Unit) : BaseRecyclerViewAdapter<SitterCardViewModel, SitterListAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val viewModel = get(position)
        holder.apply(viewModel)
        holder.itemView.setOnClickListener { sitterClickedListener(viewModel.sitter) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.sitter_card, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        @BindView(R.id.main_thumbnail) lateinit var mainThumbnail: ImageView
        @BindView(R.id.user_thumbnail) lateinit var userThumbnail: ImageView
        @BindView(R.id.nickname) lateinit var nickName: TextView
        @BindView(R.id.rating_bar) lateinit var ratingBar: RatingBar
        @BindView(R.id.rating) lateinit var rating: TextView
        @BindView(R.id.introduction_message) lateinit var introductionMessage: TextView
        @BindView(R.id.acceptable_dog_size) lateinit var acceptableDogSize: TextView

        init {
            ButterKnife.bind(this, view)
        }

        fun apply(viewModel: SitterCardViewModel) {
            viewModel.mainThumbnailUrl?.let { mainThumbnail.load(it) }
            userThumbnail.load(viewModel.userThumbnailUrl, RequestOptions.circleCropTransform())
            nickName.text = viewModel.nickName
            ratingBar.rating = viewModel.rating
            rating.text = viewModel.ratingText
            introductionMessage.text = viewModel.introductionMessage
            acceptableDogSize.text = viewModel.acceptableDogSize
        }

    }

}

data class SitterCardViewModel(
        val sitter: Sitter,
        val id: String,
        val mainThumbnailUrl: String?,
        val userThumbnailUrl: String,
        val nickName: String,
        val rating: Float,
        val ratingText: String,
        val introductionMessage: String,
        val acceptableDogSize: String) {

    companion object {

        fun from(sitter: Sitter, context: Context) = SitterCardViewModel(
                sitter = sitter,
                id = sitter.userId,
                mainThumbnailUrl = sitter.mainImage,
                userThumbnailUrl = sitter.user.iconUri,
                nickName = sitter.user.nickName,
                rating = sitter.scoreAverage.toFloat(),
                ratingText = sitter.scoreAverage.toFloat().toString(),
                introductionMessage = sitter.introductionMessage,
                acceptableDogSize = acceptableDogSizeMessage(sitter.acceptableDogSizes, context)
        )

        private fun acceptableDogSizeMessage(acceptableDogSizes: List<Dog.SizeType>, context: Context): String {
            fun okOrNg(exists: Boolean) = context.getString(if (exists) R.string.ok else R.string.ng)
            val existsSmall = acceptableDogSizes.find { it == Dog.SizeType.Small } != null
            val existsMedium = acceptableDogSizes.find { it == Dog.SizeType.Medium } != null
            val existsLarge = acceptableDogSizes.find { it == Dog.SizeType.Large } != null
            return context.getString(R.string.acceptable_dog_size_message,
                    okOrNg(existsSmall), okOrNg(existsMedium), okOrNg(existsLarge))
        }


    }

}