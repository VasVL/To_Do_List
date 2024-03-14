package com.example.to_dolist.data

import java.util.Date

data class ToDoItem(
    val id: Long,
    var deal: String,
    var isDone: Boolean,
    val creationDate: Date,
    var changeDate: Date?
)
