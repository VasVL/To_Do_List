package com.example.to_dolist.ui.allDealsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.to_dolist.Repo
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.repository.ToDoItemRepository

class AllDealsViewModel(
    private val toDoItemRepository: ToDoItemRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _deals = MutableLiveData<List<ToDoItem>>()
    val deals: LiveData<List<ToDoItem>> get() = _deals

    private val repositoryCallback = {
        _deals.value = toDoItemRepository.deals.filter { !it.isDone }
    }
    init {
        toDoItemRepository.registerOnChangeToDoList(repositoryCallback)
    }

    fun onDone(toDoItem: ToDoItem) {
        toDoItemRepository.changeDeal(toDoItem.copy(isDone = !toDoItem.isDone))
    }

    fun onChoose(toDoItem: ToDoItem) {

    }

    override fun onCleared() {
        super.onCleared()
        toDoItemRepository.unregisterOnChangeToDoList(repositoryCallback)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val savedStateHandle = createSavedStateHandle()
                val toDoItemRepository = Repo.toDoItemRepository
                AllDealsViewModel(
                    toDoItemRepository = toDoItemRepository,
                    savedStateHandle = savedStateHandle
                )
            }
        }
    }
}