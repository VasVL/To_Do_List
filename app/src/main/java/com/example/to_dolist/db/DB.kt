package com.example.to_dolist.db

import com.example.to_dolist.data.ToDoItem
import java.util.GregorianCalendar

class DB {
    companion object {
        // TODO: Переделать на flow
        val _deals: MutableList<ToDoItem> = mutableListOf(
            ToDoItem(1, "q", ToDoItem.DealImportance.HIGH, false, null, null),
            ToDoItem(2, "qw", ToDoItem.DealImportance.LOW, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(3, "qwe", ToDoItem.DealImportance.AVERAGE, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(4, "qwerqwertyuiq wertyuiqwertyuiqwe rtyuiqwertyuiqw ertyuiqwertyuiqw ertyuiqwertyuiq wertyuiqwertyui", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(5, "qwert", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(6, "qwerty", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(7, "qwertyu", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(8, "qwertyui", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(9, "qwertyuio", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(10, "qwertyuiqwertyui", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(11, "", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(12, "", ToDoItem.DealImportance.LOW, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(13, "", ToDoItem.DealImportance.AVERAGE, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(14, "qwertyui", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(15, "qwertyui", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(16, "qwertyuiqwertyuiqwertyui", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(17, "qwertyuiqwertyuiqwertyui", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(18, "qwertyui", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(19, "qwertyui", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(20, "qwertyuiqwertyui", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
        )

        fun addDeal(deal: ToDoItem): Boolean {
            val isAdded = _deals.add(deal)
            return isAdded
        }

        fun changeDeal(deal: ToDoItem): Boolean {
            val index = _deals.indexOfFirst { it.id == deal.id }
            if (index == -1) return false
            _deals[index] = deal
            return true
        }

        fun filterDeals(isDoneShowed: Boolean): List<ToDoItem> {
            return if (isDoneShowed) _deals.toList()
            else _deals.filter { !it.isDone }
        }
    }
}