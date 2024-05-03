package com.example.to_dolist.repository

import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.db.dao.ToDoItemsDao
import com.example.to_dolist.db.entity.ToDoItemEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ToDoItemRepository(
    private val dispatcherIO: CoroutineDispatcher,
    private val externalScope: CoroutineScope,
    private val toDoItemsDao: ToDoItemsDao,
) {

    // todo мб сделать доступ к кэшу через лок (пока кэш вообще не нужен)
    //private var catchList: List<ToDoItem> = listOf()

    init {
        externalScope.launch(dispatcherIO) {
            toDoItemsDao.getAll().collect { list ->
                _deals.emit(list.map { it.toToDoItem() })
            }
        }
    }

    private val _deals: MutableSharedFlow<List<ToDoItem>> = MutableSharedFlow (
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val deals: Flow<List<ToDoItem>> get() = _deals

    suspend fun addDeal(deal: ToDoItem) {
        withContext(dispatcherIO) {
            toDoItemsDao.insert(ToDoItemEntity.fromToDoItem(deal))
        }
    }

    suspend fun changeDeal(deal: ToDoItem) {
        withContext(dispatcherIO) {
            toDoItemsDao.update(ToDoItemEntity.fromToDoItem(deal))
        }
    }

    suspend fun deleteDeal(deal: ToDoItem) {
        withContext(dispatcherIO) {
            toDoItemsDao.delete(ToDoItemEntity.fromToDoItem(deal))
        }
    }
}