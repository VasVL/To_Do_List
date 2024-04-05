package com.example.to_dolist.ui.changeDealScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.to_dolist.Repo
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.repository.ToDoItemRepository
import com.example.to_dolist.ui.allDealsScreen.AllDealsViewModel

class ChangeDealViewModel(
    private val toDoItemRepository: ToDoItemRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val id = savedStateHandle.get<Long>(DEAL_ID) ?: -1L

    private val _deal = MutableLiveData<ToDoItem>()
    val deal: LiveData<ToDoItem> get() = _deal

    init {
        if (id != -1L) _deal.value = toDoItemRepository.getDeal(id)
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

        private const val DEAL_ID = "DEAL_ID"
    }
}