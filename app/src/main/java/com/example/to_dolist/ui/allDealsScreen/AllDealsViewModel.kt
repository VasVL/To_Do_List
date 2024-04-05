package com.example.to_dolist.ui.allDealsScreen

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
import com.example.to_dolist.db.DB
import com.example.to_dolist.repository.OnChangeToDoListCallback
import com.example.to_dolist.repository.ToDoItemRepository

class AllDealsViewModel(
    private val toDoItemRepository: ToDoItemRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _deals = MutableLiveData<List<ToDoItem>>()

    val deals: LiveData<List<ToDoItem>> get() = _deals

    private val dealsChangeCallback: OnChangeToDoListCallback = { list ->
        _deals.value = if (isDoneShowed.value == true) DB._deals.toList()
        else DB._deals.filter { !it.isDone }
    }

    private val dealsCountChangeCallback: OnChangeToDoListCallback = { list ->
        doneCount.value = list.count { it.isDone }
    }

    private val callbacks = mutableListOf<OnChangeToDoListCallback>()

    // TODO: Можно вынести в настройки
    val isDoneShowed = savedStateHandle.getLiveData<Boolean>(IS_DONE_SHOWED, false)
    val doneCount = savedStateHandle.getLiveData<Int>(DONE_COUNT)

    init {
        callbacks.add(dealsChangeCallback)
        callbacks.add(dealsCountChangeCallback)
        callbacks.forEach {
            toDoItemRepository.registerOnChangeToDoList(it)
        }
    }

    fun onDone(toDoItem: ToDoItem) {
        toDoItemRepository.changeDeal(toDoItem.copy(isDone = !toDoItem.isDone))
    }

    fun onChoose(toDoItem: ToDoItem) {

    }

    fun showOrHideDone() {
        isDoneShowed.value = !isDoneShowed.value!!
        toDoItemRepository.filterDeals(isDoneShowed.value!!)
    }

    override fun onCleared() {
        super.onCleared()
        callbacks.forEach {
            toDoItemRepository.unregisterOnChangeToDoList(it)
        }
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