package com.wanpaku.pochi.app.sitterdetail

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import com.bumptech.glide.request.RequestOptions
import com.wanpaku.pochi.R
import com.wanpaku.pochi.domain.sitter.Review
import com.wanpaku.pochi.infra.ui.widget.load

class SitterReviewSummaryView @JvmOverloads constructor(context: Context,
                                                        attrs: AttributeSet? = null,
                                                        defStyleAttr: Int = 0,
                                                        defStyleRes: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val title: TextView
    private val thumbnail: ImageView
    private val fullName: TextView
    private val nickName: TextView
    private val ratingBar: RatingBar
    private val comment: TextView
    private val date: TextView
    private val readMore: TextView
    private val divider: View


    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_sitter_review_summary, this)
        title = view.findViewById(R.id.title)
        thumbnail = view.findViewById(R.id.thumbnail)
        fullName = view.findViewById(R.id.full_name)
        nickName = view.findViewById(R.id.nickname)
        ratingBar = view.findViewById(R.id.rating_bar)
        comment = view.findViewById(R.id.comment)
        date = view.findViewById(R.id.date)
        readMore = view.findViewById(R.id.read_more)
        divider = view.findViewById(R.id.divider)
    }

    fun apply(viewModel: ViewModel) {
        title.text = viewModel.title
        viewModel.thumbnailUrl?.let { thumbnail.load(it, RequestOptions.centerCropTransform()) }
        viewModel.fullName?.let { fullName.text = it }
        viewModel.nickName?.let { nickName.text = it }
        ratingBar.rating = viewModel.rating
        comment.text = viewModel.comment
        date.text = viewModel.date
        readMore.setOnClickListener { viewModel.readMoreClickListener() }
        readMore.visibility = viewModel.readMoreVisibility
        divider.visibility = viewModel.readMoreVisibility
    }

    class ViewModel(review: Review, reviewCount: Int, context: Context, val readMoreClickListener: () -> Unit) {
        val title = context.getString(R.string.review_count, reviewCount)
        val thumbnailUrl = review.reviewer?.iconUri
        val fullName = review.reviewer?.fullName()
        val nickName = review.reviewer?.nickName
        val rating = review.score.toFloat()
        val comment = review.comment
        // TOOD
        val date = review.createdAtString()
        val readMoreVisibility = if (reviewCount > 1) View.VISIBLE else View.GONE
    }

}
