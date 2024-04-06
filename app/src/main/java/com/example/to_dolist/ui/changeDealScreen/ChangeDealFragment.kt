package com.example.to_dolist.ui.changeDealScreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.to_dolist.R
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.databinding.FragmentChangeDealBinding
import com.example.to_dolist.util.dateOrNullFromString
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
            val toDoItem = viewModel.deal.value
            toDoItem?.let { toDo ->
                whatToDo.setText(toDo.deal)
                importance.text = when (toDo.importance) {
                    ToDoItem.DealImportance.HIGH -> { "Важно" }
                    ToDoItem.DealImportance.LOW -> { "Не важно" }
                    ToDoItem.DealImportance.AVERAGE -> { "Нет" }
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
                deleteText.setTextColor(requireContext().resources.getColor(R.color.red))
                deleteText.setOnClickListener {
                    viewModel.delete()
                    findNavController().navigateUp()
                }
                deleteImage.setOnClickListener {
                    viewModel.delete()
                    findNavController().navigateUp()
                }

                deadlineEnable.setOnCheckedChangeListener { compoundButton, isChecked ->
                    if (isChecked) {
                        val datePicker =
                            MaterialDatePicker.Builder.datePicker()
                                .setTitleText("Дедлайн")
                                .setSelection(if (toDo.deadline != null) toDo.deadline!!.time else System.currentTimeMillis())
                                .build()
                        datePicker.addOnPositiveButtonClickListener {
                            deadlineDate.text = Date(it).format() // todo переделать на слушателей вьюмодели
                            deadlineDate.visibility = View.VISIBLE
                        }
                        datePicker.addOnNegativeButtonClickListener { deadlineEnable.isChecked = false }
                        datePicker.addOnCancelListener { deadlineEnable.isChecked = false }
                        datePicker.show(childFragmentManager, "tag")
                    } else {
                        deadlineDate.visibility = View.GONE
                    }
                }
            }



            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.itemSave -> {
                        viewModel.save(
                            deal = whatToDo.text.toString(),
                            importance = when (importance.text) {
                                "Важно" -> ToDoItem.DealImportance.HIGH
                                "Не важно" -> ToDoItem.DealImportance.LOW
                                else -> ToDoItem.DealImportance.AVERAGE
                            },
                            deadline = if (deadlineEnable.isChecked) dateOrNullFromString(deadlineDate.text.toString()) else null
                        )
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

}