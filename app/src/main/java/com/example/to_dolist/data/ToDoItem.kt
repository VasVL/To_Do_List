package com.example.to_dolist.data

import com.example.to_dolist.R
import java.util.Date

data class ToDoItem(
    val id: Long,
    var deal: String,
    var importance: DealImportance,
    var isDone: Boolean,
    var deadline: Date?,
    var changeDate: Date?,
) {

    constructor() : this(-1, "", ToDoItem.DealImportance.AVERAGE, false, null, null)

    fun getDealWithImportance(): String {
        return when(importance){
            DealImportance.LOW -> "\u2B07 "
            DealImportance.AVERAGE -> ""
            DealImportance.HIGH -> "\u203C "
        } + deal
    }

    enum class DealImportance {
        LOW,
        AVERAGE,
        HIGH,
    }
}
