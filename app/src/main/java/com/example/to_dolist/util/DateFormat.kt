package com.example.to_dolist.util

import java.util.Date

fun Date.format(): String {
    val builder = StringBuilder()
    builder.append("${date}")
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

fun dateOrNullFromString(str: String): Date? {

    if (str.isEmpty()) return null

    val list = str.split(" ")
    val year = list[2].toIntOrNull() ?: 1900
    val month = when (list[1]) {
            "января" -> 0
            "февраля" -> 1
            "марта" -> 2
            "апреля" -> 3
            "мая" -> 4
            "июня" -> 5
            "июля" -> 6
            "августа" -> 7
            "сентября" -> 8
            "октября" -> 9
            "ноября" -> 10
            "декабря" -> 11
            else -> 0
        }
    val day = list[0].toIntOrNull() ?: 1

    return Date(year - 1900, month, day)
}