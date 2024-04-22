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
import com.example.to_dolist.repository.OnChangeToDoListCallback
import com.example.to_dolist.repository.ToDoItemRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AllDealsViewModel(
    private val toDoItemRepository: ToDoItemRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val _deals = MutableLiveData<List<ToDoItem>>()

    val deals: LiveData<List<ToDoItem>> get() = _deals

    private val dealsChangeCallback: OnChangeToDoListCallback = { list ->
        viewModelScope.launch {
            if (isDoneShowed.value == true) _deals.value = list
            else {
                // todo Возможно лучше вынимать все дела, а фильтровать во фрагменте обозревая isDoneShowed
                var filtered = listOf<ToDoItem>()
                withContext(Dispatchers.Default) {
                    filtered = list.filter { !it.isDone }
                }
                _deals.value = filtered
            }
        }
    }

    private val dealsCountChangeCallback: OnChangeToDoListCallback = { list ->
        viewModelScope.launch {
            doneCount.value = withContext(Dispatchers.Default) { list.count { it.isDone } }
        }

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
        toDoItemRepository.filterDeals() // todo Тоже не нравится мне как сделано см. dealsChangeCallback
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