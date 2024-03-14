package com.example.to_dolist.data

import com.example.to_dolist.R
import java.util.Date

data class ToDoItem(
    val id: Long,
    var deal: String,
    var importance: DealImportance,
    var isDone: Boolean,
    var deadline: Date,
    var changeDate: Date?,
) {

    fun getDealWithImportance(): String {
        return when(importance){
            DealImportance.LOW -> "\u203C "
            DealImportance.AVERAGE -> ""
            DealImportance.HIGH -> "\u2B07 "
        } + deal
    }

    enum class DealImportance {
        LOW,
        AVERAGE,
        HIGH,
    }
}
