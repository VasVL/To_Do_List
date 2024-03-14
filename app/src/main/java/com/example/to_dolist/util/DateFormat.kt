package com.example.to_dolist.util

import java.util.Date

fun Date.format(): String {
    val builder = StringBuilder()
    builder.append("${day}")
    builder.append(
        when (month) {
            0 -> " января "
            1 -> " февраля "
            2 -> " марта "
            3 -> " апреля "
            4 -> " мая "
            5 -> " июня "
            6 -> " июля "
            7 -> " августа "
            8 -> " сентября "
            9 -> " октября "
            10 -> " ноября "
            11 -> " декабря "
            else -> " ??? "
        }
    )
    builder.append("${year + 1900}")

    return builder.toString()
}