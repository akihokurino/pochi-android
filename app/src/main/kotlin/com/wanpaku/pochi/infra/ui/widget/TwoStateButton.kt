package com.wanpaku.pochi.infra.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.Button
import com.wanpaku.pochi.R


class TwoStateButton : Button {

    var activeBackground: Drawable? = null
    var inactiveBackground: Drawable? = null
    var isActive = false
        set(value) {
            if (field != value) {
                field = value
                refresh()
            }
        }

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, style: Int = 0) : super(context, attrs, style) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.TwoStateButton, style, 0)
        activeBackground = a.getDrawable(R.styleable.TwoStateButton_activeBackground)
        inactiveBackground = a.getDrawable(R.styleable.TwoStateButton_inactiveBackground)
        isActive = a.getBoolean(R.styleable.TwoStateButton_isActive, false)
        a.recycle()

        refresh()
    }

    private fun refresh() {
        isClickable = isActive
        when {
            isActive && activeBackground != null -> setBackgroundDrawable(activeBackground)
            !isActive && inactiveBackground != null -> setBackgroundDrawable(inactiveBackground)
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        super.setOnClickListener {
            if (isActive) l?.onClick(this)
        }
    }
}