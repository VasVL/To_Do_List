package com.example.to_dolist.repository

import com.example.to_dolist.db.DB
import com.example.to_dolist.data.ToDoItem

typealias OnChangeToDoListCallback = (List<ToDoItem>) -> Unit

class ToDoItemRepository {

    private val _deals: List<ToDoItem>
        // TODO: хуита
        get() = if (_isDoneShowed) DB._deals.toList()
        else DB._deals.filter { !it.isDone }
    private var _isDoneShowed = false

    private val callbacks = mutableListOf<OnChangeToDoListCallback>()

    fun addDeal(deal: ToDoItem): Boolean {
        val isAdded = DB.addDeal(deal)
        if (isAdded) {
            notifyToDoListChanged()
        }
        return isAdded
    }

    fun changeDeal(deal: ToDoItem): Boolean {
        val isOk = DB.changeDeal(deal)
        if (isOk) {
            notifyToDoListChanged()
            return true
        }

        return false
    }

    fun filterDeals(isDoneShowed: Boolean) {
        _isDoneShowed = isDoneShowed
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