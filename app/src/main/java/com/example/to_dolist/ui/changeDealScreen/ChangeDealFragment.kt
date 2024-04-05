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
import com.example.to_dolist.util.format

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
                if (deadline != null) {
                    deadlineDate.text = deadline.format()
                    deadlineDate.visibility = View.VISIBLE
                }
                deleteText.setTextColor(requireContext().resources.getColor(R.color.red))
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
                            deadline = viewModel.deal.value?.deadline // todo
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}