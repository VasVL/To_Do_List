package com.example.to_dolist.ui.changeDealScreen

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
import kotlinx.coroutines.launch

class ChangeDealViewModel(
    private val toDoItemRepository: ToDoItemRepository,
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {

    private val _deal = savedStateHandle.getLiveData<ToDoItem>(DEAL)
    val deal: LiveData<ToDoItem> get() = _deal

    // todo показывать заглушку по типау "сохраняется/удаляется"
    //  или просто крутилку
    private val _isWorkDone = MutableLiveData<Boolean>(false)
    val isWorkDone: LiveData<Boolean> get() = _isWorkDone


    fun changeDeal(newDeal: ToDoItem) {
        _deal.value = newDeal
    }

    fun save(text: String) {
        viewModelScope.launch {
            if (deal.value!!.id != 0L) {
                toDoItemRepository.changeDeal(deal.value!!.copy(text = text))
            } else {
                toDoItemRepository.addDeal(deal.value!!.copy(text = text))
            }
            _isWorkDone.value = true
        }
    }

    fun delete() {
        viewModelScope.launch {
            toDoItemRepository.deleteDeal(deal.value!!)
            _isWorkDone.value = true
        }
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