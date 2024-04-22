package com.example.to_dolist.repository

import com.example.to_dolist.db.DB
import com.example.to_dolist.data.ToDoItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlin.coroutines.coroutineContext

typealias OnChangeToDoListCallback = (List<ToDoItem>) -> Unit

class ToDoItemRepository(
    val dispatcherIO: CoroutineDispatcher
) {

    private val _deals: List<ToDoItem>
        // TODO: хуита
        get() = DB._deals.sortedBy { it.deadline }

    private val callbacks = mutableListOf<OnChangeToDoListCallback>()

    suspend fun addDeal(deal: ToDoItem): Boolean {
        var isOk = false
        // Здесь не использую = withContext(...) для того, чтоб можно было спокойно вызывать из main потока
        withContext(dispatcherIO) {
            isOk = DB.addDeal(deal)
        }
        if (isOk) notifyToDoListChanged()
        return isOk
    }

    suspend fun changeDeal(deal: ToDoItem): Boolean {
        var isOk = false
        withContext(dispatcherIO) {
            isOk = DB.changeDeal(deal)
        }
        if (isOk) notifyToDoListChanged()
        return isOk
    }

    suspend fun deleteDeal(id: Long): Boolean {
        var isOk = false
        withContext(dispatcherIO) {
            isOk = DB.deleteDeal(id)
        }
        if (isOk) notifyToDoListChanged()
        return isOk
    }

    fun filterDeals() {
        notifyToDoListChanged()
    }

    fun registerOnChangeToDoList(callback: OnChangeToDoListCallback) {
        callbacks.add(callback)
        callback.invoke(_deals)
    }

    fun unregisterOnChangeToDoList(callback: OnChangeToDoListCallback) {
        callbacks.remove(callback)
    }

    private fun notifyToDoListChanged() {
        callbacks.forEach { it.invoke(_deals) }
    }
}