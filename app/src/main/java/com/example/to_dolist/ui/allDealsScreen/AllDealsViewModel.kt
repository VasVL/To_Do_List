package com.example.to_dolist.ui.allDealsScreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.to_dolist.Repo
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.repository.ToDoItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllDealsViewModel(
    private val toDoItemRepository: ToDoItemRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _deals = MutableLiveData<List<ToDoItem>>(listOf())

    val deals: LiveData<List<ToDoItem>> get() = _deals

    // TODO: Можно вынести в настройки
    val isDoneShowed = savedStateHandle.getLiveData<Boolean>(IS_DONE_SHOWED, false)
    val doneCount = savedStateHandle.getLiveData<Int>(DONE_COUNT)

    init {
        viewModelScope.launch {
            toDoItemRepository.deals.collect { list ->
                _deals.value = list
                withContext(Dispatchers.Default) {
                    doneCount.postValue(list.count { it.isDone })
                }
            }
        }
    }

    fun onDone(toDoItem: ToDoItem) {
        viewModelScope.launch {
            toDoItemRepository.changeDeal(toDoItem.copy(isDone = !toDoItem.isDone))
        }
    }

    fun onDelete(toDoItem: ToDoItem) {
        viewModelScope.launch {
            toDoItemRepository.deleteDeal(toDoItem.id)
        }
    }

    fun showOrHideDone() {
        isDoneShowed.value = !isDoneShowed.value!!
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

        private const val IS_DONE_SHOWED = "IS_DONE_SHOWED"
        private const val DONE_COUNT = "DONE_COUNT"
    }
}