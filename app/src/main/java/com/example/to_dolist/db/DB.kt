package com.example.to_dolist.db

import com.example.to_dolist.data.ToDoItem
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

class DB {
    companion object {
        // TODO: Переделать на flow
        val _deals: MutableList<ToDoItem> = mutableListOf(
            ToDoItem(1, "q", ToDoItem.DealImportance.HIGH, false, null, null),
            ToDoItem(2, "qw", ToDoItem.DealImportance.LOW, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(3, "qwe", ToDoItem.DealImportance.AVERAGE, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(4, "qwerqwertyuiq wertyuiqwertyuiqwe rtyuiqwertyuiqw ertyuiqwertyuiqw ertyuiqwertyuiq wertyuiqwertyui wwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwwww", ToDoItem.DealImportance.HIGH, false, GregorianCalendar(2022, 3, 1).time, null),
            ToDoItem(5, "qwert", ToDoItem.DealImportance.HIGH, false, null, null),
            ToDoItem(6, "qwerty", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(7, "qwertyu", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 2), null),
            ToDoItem(8, "qwertyui", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 3), null),
            ToDoItem(9, "qwertyuio", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 4), null),
            ToDoItem(10, "qwertyuiqwertyui", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(11, "", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(12, "", ToDoItem.DealImportance.LOW, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(13, "", ToDoItem.DealImportance.AVERAGE, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(14, "qwertyui", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(15, "qwertyui", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(16, "qwertyuiqwertyuiqwertyui", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(17, "qwertyuiqwertyuiqwertyui", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(18, "qwertyui", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(19, "qwertyui", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 1), null),
            ToDoItem(20, "qwertyuiqwertyui", ToDoItem.DealImportance.HIGH, false, Date(2022 - 1900, 3, 1), null),
        )

        fun addDeal(deal: ToDoItem): Boolean {
            return _deals.add(deal.copy(id = _deals.size.toLong()))
        }

        fun changeDeal(deal: ToDoItem): Boolean {
            val index = _deals.indexOfFirst { it.id == deal.id }
            if (index == -1) return false
            _deals[index] = deal
            return true
        }

        fun deleteDeal(id: Long): Boolean {
            return _deals.removeAll { it.id == id }
        }
    }
}