package com.example.to_dolist.data

import java.util.Date

data class ToDoItem(
    val id: Long,
    var deal: String,
    val importance: DealImportance,
    var isDone: Boolean,
    val deadline: Date,
    var changeDate: Date?,
) {
    enum class DealImportance {
        LOW,
        AVERAGE,
        HIGH,
    }
}
