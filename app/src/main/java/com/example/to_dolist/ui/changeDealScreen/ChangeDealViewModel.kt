package com.example.to_dolist.ui.changeDealScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.to_dolist.Repo
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.repository.ToDoItemRepository

class ChangeDealViewModel(
    private val toDoItemRepository: ToDoItemRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _deal = savedStateHandle.getLiveData<ToDoItem>(DEAL)
    val deal: LiveData<ToDoItem> get() = _deal

    fun changeDeal(newDeal: ToDoItem) {
        _deal.value = newDeal
    }

    fun save(text: String) {
        if (deal.value!!.id != -1L) {
            toDoItemRepository.changeDeal(deal.value!!.copy(text = text))
        } else {
            toDoItemRepository.addDeal(deal.value!!.copy(text = text))
        }
    }

    fun delete() {
        toDoItemRepository.deleteDeal(deal.value!!.id)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val toDoItemRepository = Repo.toDoItemRepository
                ChangeDealViewModel(
                    toDoItemRepository = toDoItemRepository,
                    savedStateHandle = savedStateHandle
                )
            }
        }

        private const val DEAL = "DEAL"
    }
}