package com.example.to_dolist.ui.changeDealScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar.OnMenuItemClickListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.to_dolist.R
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.databinding.FragmentChangeDealBinding
import com.example.to_dolist.util.format
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Date

class ChangeDealFragment : Fragment() {


    private val viewModel: ChangeDealViewModel by viewModels { ChangeDealViewModel.Factory }

    private var _binding: FragmentChangeDealBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChangeDealBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            // Смотрим вьюмодель - обновляем стейт виджетов
            viewModel.deal.observe(viewLifecycleOwner, toDoItemObserver)

            val toDoItem = viewModel.deal.value!!
            // EditText сам хранит свой стейт, его в обсервере не смотрим
            whatToDo.setText(toDoItem.text)

            // Удалять новое только что созданное дело не имеет смысла, так что вот
            if (toDoItem.id != -1L)  {
                deleteText.setTextColor(requireContext().resources.getColor(R.color.red))
                deleteView.setOnClickListener(deleteClickListener)
            } else {
                deleteView.isClickable = false
            }

            // Устанавливаем дедлайн
            deadlineDate.setOnClickListener { showDatePicker(toDoItem) }
            deadlineEnable.setOnClickListener {
                if (deadlineEnable.isChecked) {
                    showDatePicker(toDoItem)
                } else {
                    viewModel.changeDeal(toDoItem.copy(deadline = null))
                }
            }
            // Баг: открывается при первом заходе на экран, если есть установленный дедлайн
//            deadlineEnable.setOnCheckedChangeListener { compoundButton, isChecked ->
//                if (isChecked) {
//                    showDatePicker(toDoItem)
//                } else {
//                    viewModel.changeDeal(toDoItem.copy(deadline = null))
//                }
//            }

            // Выбираем важность
            importanceView.setOnClickListener { showPopupMenu() }

            // Можем сохранить изменения/новое дело, либо выйти
            toolbar.setOnMenuItemClickListener(onMenuItemClickListener)
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            // От руки рисуем тень под тулбаром
            scrollView.setOnScrollChangeListener(toolbarShadowScrollListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private val toolbarShadowScrollListener = View.OnScrollChangeListener { _, _, y1, _, _ ->
        binding.toolbarShadow.visibility = if (y1 == 0) {
            View.INVISIBLE
        } else {
            View.VISIBLE
        }
    }

    private val onMenuItemClickListener: OnMenuItemClickListener = OnMenuItemClickListener {
        when (it.itemId) {
            R.id.itemSave -> {
                val dealText = binding.whatToDo.text.toString()
                viewModel.save(dealText)
                findNavController().navigateUp()
                true
            }
            else ->{
                false
            }
        }
    }

    private val toDoItemObserver: Observer<ToDoItem> = Observer{ toDo ->
        setImportance(toDo.importance)
        setDeadline(toDo.deadline)
    }

    private fun setImportance(importance: ToDoItem.DealImportance) {
        when (importance) {
            ToDoItem.DealImportance.HIGH -> {
                binding.importance.text = "Высокий"
                binding.importance.setTextColor(requireContext().resources.getColor(R.color.red))
            }
            ToDoItem.DealImportance.LOW -> {
                binding.importance.text = "Низкий"
                binding.importance.setTextColor(requireContext().resources.getColor(R.color.black))
            }
            ToDoItem.DealImportance.AVERAGE -> {
                binding.importance.text = "Нет"
                binding.importance.setTextColor(requireContext().resources.getColor(R.color.gray))
            }
        }
    }

    private fun setDeadline(deadline: Date?) {
        binding.deadlineEnable.isChecked = if (deadline != null) {
            binding.deadlineDate.text = deadline.format()
            binding.deadlineDate.visibility = View.VISIBLE
            true
        } else {
            binding.deadlineDate.visibility = View.GONE
            false
        }
    }

    private val deleteClickListener: OnClickListener = OnClickListener {
        AlertDialog.Builder(requireContext())
            .setTitle("Удалить дело?")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.delete()
                findNavController().navigateUp()
            }
            .setNegativeButton("NOOO!!!") { _, _ -> }
            .create()
            .show()
    }

    private fun showDatePicker(toDoItem: ToDoItem) {
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Дедлайн")
                .setSelection(if (toDoItem.deadline != null) toDoItem.deadline!!.time else System.currentTimeMillis())
                .build()
        datePicker.addOnPositiveButtonClickListener {
            viewModel.changeDeal(toDoItem.copy(deadline = Date(it)))
        }
        datePicker.show(childFragmentManager, "tag")
    }

    /**
     * Меню выбора важности дела
     * */
    private fun showPopupMenu() {
        val popupMenu = PopupMenu(requireContext(), binding.importance)
        popupMenu.inflate(R.menu.menu_importance)

        val deal = viewModel.deal.value!!

        popupMenu.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.itemImportanceAverage -> {
                    viewModel.changeDeal(deal.copy(importance = ToDoItem.DealImportance.AVERAGE))
                }
                R.id.itemImportanceLow -> {
                    viewModel.changeDeal(deal.copy(importance = ToDoItem.DealImportance.LOW))
                }
                R.id.itemImportanceHigh -> {
                    viewModel.changeDeal(deal.copy(importance = ToDoItem.DealImportance.HIGH))
                }
            }
            true
        }

        popupMenu.show()
    }

}