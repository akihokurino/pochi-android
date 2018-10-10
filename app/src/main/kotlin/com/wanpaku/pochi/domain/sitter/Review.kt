package com.wanpaku.pochi.domain.sitter

import com.wanpaku.pochi.domain.user.UserOverView
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

data class Review(val reviewer: UserOverView?,
                  val score: Int,
                  val comment: String,
                  val createdAt: DateTime) {

    fun createdAtString() = DateTimeFormat.forPattern("yyyy年MM月dd日").print(createdAt)

}