package com.example.to_dolist.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.to_dolist.data.ToDoItem
import java.util.Date

@Entity(tableName = "deals")
data class ToDoItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long,
    var text: String,
    var importance: ToDoItem.DealImportance,
    var isDone: Boolean,
    var deadline: Date?,
) {
    fun toToDoItem(): ToDoItem = ToDoItem(
        id = id,
        text = text,
        importance = importance,
        isDone = isDone,
        deadline = deadline,
    )

    companion object {
        fun fromToDoItem(item: ToDoItem): ToDoItemEntity = ToDoItemEntity(
            id = item.id,
            text = item.text,
            importance = item.importance,
            isDone = item.isDone,
            deadline = item.deadline,
        )
    }
}