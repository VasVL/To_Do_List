package com.example.to_dolist

import com.example.to_dolist.repository.ToDoItemRepository
import kotlinx.coroutines.Dispatchers

object Repo {
    val toDoItemRepository = ToDoItemRepository(dispatcherIO = Dispatchers.IO)
}