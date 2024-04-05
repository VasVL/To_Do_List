package com.example.to_dolist.repository

import com.example.to_dolist.db.DB
import com.example.to_dolist.data.ToDoItem

typealias OnChangeToDoListCallback = (List<ToDoItem>) -> Unit

class ToDoItemRepository {

    private val _deals: List<ToDoItem>
        // TODO: хуита
        get() = DB._deals.toList()

    private val callbacks = mutableListOf<OnChangeToDoListCallback>()

    fun getDeal(id: Long): ToDoItem? {
        return _deals.firstOrNull { it.id == id }
    }

    fun addDeal(deal: ToDoItem): Boolean {
        val isOk = DB.addDeal(deal)
        if (isOk) notifyToDoListChanged()
        return isOk
    }

    fun changeDeal(deal: ToDoItem): Boolean {
        val isOk = DB.changeDeal(deal)
        if (isOk) notifyToDoListChanged()
        return isOk
    }

    fun deleteDeal(id: Long): Boolean {
        val isOk =  DB.deleteDeal(id)
        if (isOk) notifyToDoListChanged()
        return isOk
    }

    fun filterDeals(isDoneShowed: Boolean) {
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