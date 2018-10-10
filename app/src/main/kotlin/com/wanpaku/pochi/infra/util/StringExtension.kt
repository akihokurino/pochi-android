package com.wanpaku.pochi.infra.util

import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.style.ClickableSpan
import android.view.View
import java.util.regex.Pattern

fun SpannableString.withLink(linkPair: Map<String, String>, urlClickListener: (title: String, url: String) -> Unit, linkTextColor: Int = Color.GRAY): SpannableString {
    return SpannableString(this).apply {
        linkPair.entries.forEach {
            val title = it.key
            val url = it.value
            val pattern = Pattern.compile(title)
            val matcher = pattern.matcher(this)
            if (!matcher.find()) return@forEach
            val start = matcher.start()
            val end = matcher.end()
            setSpan(object : ClickableSpan() {

                override fun onClick(view: View) {
                    urlClickListener(title, url)
                }

                override fun updateDrawState(ds: TextPaint) {
                    super.updateDrawState(ds)
                    ds.color = linkTextColor
                }

            }, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
        }
    }
}
