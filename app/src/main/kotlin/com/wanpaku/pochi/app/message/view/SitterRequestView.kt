package com.wanpaku.pochi.app.message.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.TextView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.wanpaku.pochi.R

class SitterRequestView @JvmOverloads constructor(context: Context,
                                                  attrs: AttributeSet? = null,
                                                  defStyleAttr: Int = 0,
                                                  defStyleRes: Int = 0)
    : RequestView(context, attrs, defStyleAttr, defStyleRes) {


    override val preRequestView: View by lazy {
        LayoutInflater.from(context).inflate(R.layout.view_sitter_prerequest, null, false)
    }
    override val requestingView: SitterRequestingView by lazy {
        SitterRequestingView(context)
    }
    override val requestAcceptedView: SitterRequestAcceptedView by lazy {
        SitterRequestAcceptedView(context)
    }

    override fun headerTitle(): Int = R.string.request_detail

}

class SitterRequestingView @JvmOverloads constructor(context: Context,
                                                     attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0,
                                                     defStyleRes: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    @BindView(R.id.dogs) lateinit var dogs: TextView
    @BindView(R.id.start_date_text) lateinit var startDateText: TextView
    @BindView(R.id.end_date_text) lateinit var endDateText: TextView
    @BindView(R.id.max_reward_point) lateinit var maxRewardPoint: TextView
    @BindView(R.id.accept) lateinit var accept: Button

    private var unbinder: Unbinder? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_sitter_requesting, this, true)
        unbinder = ButterKnife.bind(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unbinder?.unbind()
    }

    fun applyViewModel(viewModel: ViewModel) {
        dogs.text = viewModel.dog
        startDateText.text = viewModel.startDate
        endDateText.text = viewModel.endDate
        maxRewardPoint.text = viewModel.point
    }

    fun setOnAccpetClickedListener(listener: () -> Unit) {
        accept.setOnClickListener { listener() }
    }

    data class ViewModel(val dog: String,
                         val startDate: String,
                         val endDate: String,
                         val point: String)

}

class SitterRequestAcceptedView @JvmOverloads constructor(context: Context,
                                                          attrs: AttributeSet? = null,
                                                          defStyleAttr: Int = 0,
                                                          defStyleRes: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    @BindView(R.id.dogs) lateinit var dogs: TextView
    @BindView(R.id.start_date_text) lateinit var startDateText: TextView
    @BindView(R.id.end_date_text) lateinit var endDateText: TextView
    @BindView(R.id.max_reward_point) lateinit var maxRewardPoint: TextView
    @BindView(R.id.user_profile) lateinit var userProfile: TextView

    private var unbinder: Unbinder? = null

    init {
        LayoutInflater.from(context).inflate(R.layout.view_sitter_requestaccepted, this, true)
        unbinder = ButterKnife.bind(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        unbinder?.unbind()
    }

    fun applyViewModel(viewModel: ViewModel) {
        dogs.text = viewModel.dog
        startDateText.text = viewModel.startDate
        endDateText.text = viewModel.endDate
        maxRewardPoint.text = viewModel.point
    }

    fun setOnUserProfileClickedListener(listener: () -> Unit) {
        userProfile.setOnClickListener { listener() }
    }

    data class ViewModel(val dog: String,
                         val startDate: String,
                         val endDate: String,
                         val point: String)

}