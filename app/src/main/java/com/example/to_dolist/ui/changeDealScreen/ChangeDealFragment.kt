package com.example.to_dolist.ui.changeDealScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.to_dolist.R
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.databinding.FragmentChangeDealBinding
import com.example.to_dolist.util.format
import com.google.android.material.datepicker.MaterialDatePicker
import java.util.Date

class ChangeDealFragment : Fragment() {

    private val args: ChangeDealFragmentArgs by navArgs()

    private val viewModel: ChangeDealViewModel by viewModels { ChangeDealViewModel.Factory }

    private var _binding: FragmentChangeDealBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

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

            viewModel.deal.observe(viewLifecycleOwner) { toDo ->
                when (toDo.importance) {
                    ToDoItem.DealImportance.HIGH -> {
                        importance.text = "Высокий"
                        binding.importance.setTextColor(requireContext().resources.getColor(R.color.red))
                    }
                    ToDoItem.DealImportance.LOW -> {
                        importance.text = "Низкий"
                        binding.importance.setTextColor(requireContext().resources.getColor(R.color.black))
                    }
                    ToDoItem.DealImportance.AVERAGE -> {
                        importance.text = "Нет"
                        binding.importance.setTextColor(requireContext().resources.getColor(R.color.gray))
                    }
                }

                val deadline = toDo.deadline
                deadlineEnable.isChecked = if (deadline != null) {
                    deadlineDate.text = deadline.format()
                    deadlineDate.visibility = View.VISIBLE
                    true
                } else {
                    deadlineDate.visibility = View.GONE
                    false
                }
            }

            val toDoItem = viewModel.deal.value!!
            whatToDo.setText(toDoItem.text)

            deleteText.setTextColor(requireContext().resources.getColor(R.color.red))
            deleteText.setOnClickListener {
                viewModel.delete() // TODO: показывать диалог подтверждение
                findNavController().navigateUp()
            }
            deleteImage.setOnClickListener {
                viewModel.delete()
                findNavController().navigateUp()
            }

            // TODO: Возможность вызвать datePicker нажатием на саму дату
            // TODO: открывается при первом заходе на экран, если есть установленный дедлайн
            deadlineEnable.setOnCheckedChangeListener { compoundButton, isChecked ->
                if (isChecked) {
                    val datePicker =
                        MaterialDatePicker.Builder.datePicker()
                            .setTitleText("Дедлайн")
                            .setSelection(if (toDoItem.deadline != null) toDoItem.deadline!!.time else System.currentTimeMillis())
                            .build()
                    datePicker.addOnPositiveButtonClickListener {
                        viewModel.changeDeal(toDoItem.copy(deadline = Date(it)))
                    }
                    datePicker.show(childFragmentManager, "tag")
                } else {
                    viewModel.changeDeal(toDoItem.copy(deadline = null))
                }
            }

            importanceView.setOnClickListener { showPopupMenu() }

            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.itemSave -> {
                        viewModel.save(whatToDo.text.toString())
                        findNavController().navigateUp()
                        true
                    }
                    else ->{
                        false
                    }
                }
            }
            toolbar.setNavigationOnClickListener { findNavController().navigateUp() }

            scrollView.setOnScrollChangeListener { _, _, y1, _, _ ->
                if (y1 == 0) {
                    toolbarShadow.visibility = View.INVISIBLE
                } else {
                    toolbarShadow.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

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