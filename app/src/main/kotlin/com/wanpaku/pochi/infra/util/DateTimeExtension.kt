package com.wanpaku.pochi.infra.util

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

fun Long.toDateTime(): DateTime = DateTime(this)

fun String.fromYYYYtoMMtoDD(): DateTime {
    val formatter = DateTimeFormat.forPattern("yyyy-MM-dd")
    return DateTime.parse(this, formatter)
}

fun DateTime.to(pattern: String): String = DateTimeFormat
        .forPattern(pattern)
        .print(this)

fun DateTime.toYYYYMMDD() = to("yyyy年MM月dd日")

fun DateTime.toYYYYtoMMtoDD() = to("yyyy-MM-dd")