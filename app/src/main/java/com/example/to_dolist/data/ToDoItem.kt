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
) : Parcelable {

    constructor() : this(0, "", DealImportance.AVERAGE, false, null)

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
