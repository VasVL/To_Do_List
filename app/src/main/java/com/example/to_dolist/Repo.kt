package com.example.to_dolist

import android.content.Context
import androidx.room.Room
import com.example.to_dolist.db.database.DealsDatabase
import com.example.to_dolist.repository.ToDoItemRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

object Repo {
    private lateinit var applicationContext: Context

    private val dealsDatabase: DealsDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            DealsDatabase::class.java,
            "DealsDatabase"
        ).build()
    }

    val toDoItemRepository: ToDoItemRepository by lazy {
        ToDoItemRepository(
            dispatcherIO = Dispatchers.IO,
            externalScope = CoroutineScope(SupervisorJob()),
            dealsDatabase.todoItemDao(),
        )
    }

    fun init(context: Context) {
        applicationContext = context
    }
}