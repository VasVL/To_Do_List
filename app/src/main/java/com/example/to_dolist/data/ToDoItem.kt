package com.example.to_dolist.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

@Parcelize
data class ToDoItem(
    val id: Long,
    var text: String,
    var importance: DealImportance,
    var isDone: Boolean,
    var deadline: Date?,
    var changeDate: Date?,
) : Parcelable {

    constructor() : this(-1, "", ToDoItem.DealImportance.AVERAGE, false, null, null)

    fun getDealWithImportance(): String {
        return when(importance){
            DealImportance.LOW -> "\u2B07 "
            DealImportance.AVERAGE -> ""
            DealImportance.HIGH -> "\u203C "
        } + text
    }

    enum class DealImportance {
        LOW,
        AVERAGE,
        HIGH,
    }
}
