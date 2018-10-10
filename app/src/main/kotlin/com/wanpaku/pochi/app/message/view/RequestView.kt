package com.wanpaku.pochi.app.message.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.wanpaku.pochi.R

abstract class RequestView @JvmOverloads constructor(context: Context,
                                                     attrs: AttributeSet? = null,
                                                     defStyleAttr: Int = 0,
                                                     defStyleRes: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val container: FrameLayout
    private val divider: View
    private val icon: ImageView

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.view_request, this, true)
        view.findViewById<View>(R.id.header).setOnClickListener {
            toggle()
        }
        view.findViewById<TextView>(R.id.title).setText(headerTitle())
        container = view.findViewById(R.id.container)
        divider = view.findViewById(R.id.divider)
        icon = view.findViewById(R.id.icon)
    }
    abstract val preRequestView: View
    abstract val requestingView: View
    abstract val requestAcceptedView: View

    abstract fun headerTitle(): Int

    private fun toggle() {
        val iconResId = if (container.visibility == View.VISIBLE) R.drawable.ic_expand_more_24dp else R.drawable.ic_expand_less_24dp
        icon.setImageResource(iconResId)
        val visibility = if (container.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        container.visibility = visibility
        divider.visibility = visibility
    }

    fun replaceView(status: Status) {
        container.removeAllViews()
        when (status) {
            Status.PreRequest -> preRequestView
            Status.Requesting -> requestingView
            Status.RequestAccepted -> requestAcceptedView
        }.let { container.addView(it) }
    }

    enum class Status {
        PreRequest,
        Requesting,
        RequestAccepted;
    }

}