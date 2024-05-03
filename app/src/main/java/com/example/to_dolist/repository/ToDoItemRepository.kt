package com.example.to_dolist.repository

import com.example.to_dolist.db.DB
import com.example.to_dolist.data.ToDoItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ToDoItemRepository(
    private val dispatcherIO: CoroutineDispatcher,
    private val externalScope: CoroutineScope
) {
    val db: DB = DB()

    // todo мб сделать доступ к кэшу через лок (пока кэш вообще не нужен)
    //private var catchList: List<ToDoItem> = listOf()

    init {
        externalScope.launch(dispatcherIO) {
            db.deals.collect { list ->
                _deals.emit(list)
                //catchList = list
            }
        }
    }

    private val _deals: MutableSharedFlow<List<ToDoItem>> = MutableSharedFlow (
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    val deals: Flow<List<ToDoItem>> get() = _deals

    suspend fun addDeal(deal: ToDoItem): Boolean {
        var isOk = false
        // Здесь не использую = withContext(...) для того, чтоб можно было спокойно вызывать из main потока
        withContext(dispatcherIO) {
            isOk = db.addDeal(deal)
        }
        return isOk
    }

    suspend fun changeDeal(deal: ToDoItem): Boolean {
        var isOk = false
        withContext(dispatcherIO) {
            isOk = db.changeDeal(deal)
        }
        return isOk
    }

    suspend fun deleteDeal(id: Long): Boolean {
        var isOk = false
        withContext(dispatcherIO) {
            isOk = db.deleteDeal(id)
        }
        return isOk
    }
}