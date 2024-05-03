package com.example.to_dolist

import com.example.to_dolist.repository.ToDoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object Repo {
    val toDoItemRepository = ToDoItemRepository(
        dispatcherIO = Dispatchers.IO,
        externalScope = CoroutineScope(SupervisorJob())
    )
}