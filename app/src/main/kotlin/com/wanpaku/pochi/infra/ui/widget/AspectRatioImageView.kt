package com.wanpaku.pochi.infra.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.wanpaku.pochi.R

class AspectRatioImageView : ImageView {

    var widthRatio: Int = 1
        set(value) {
            if (value <= 0) return
            field = value
        }
    var heightRatio: Int = 1
        set(value) {
            if (value <= 0) return
            field = value
        }

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, style: Int = 0) : super(context, attrs, style) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.AspectRatioImageView, style, 0)
        widthRatio = a.getInt(R.styleable.AspectRatioImageView_widthRatio, 1)
        heightRatio = a.getInt(R.styleable.AspectRatioImageView_heightRatio, 1)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = heightRatio * measuredWidth / widthRatio.toFloat()
        setMeasuredDimension(measuredWidth, height.toInt())
    }

}