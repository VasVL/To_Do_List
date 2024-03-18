package com.example.to_dolist.ui.allDealsScreen

import android.os.Bundle
import android.view.CollapsibleActionView
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.example.to_dolist.R
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.databinding.FragmentAllDealsBinding
import com.example.to_dolist.util.OffsetItemDecoration
import com.google.android.material.appbar.CollapsingToolbarLayout

class AllDealsFragment : Fragment() {

    private var _binding: FragmentAllDealsBinding? = null
    private val binding get() = _binding!!

    private var _adapter: AllDealsAdapter? = null
    private val adapter get() = _adapter!!

    private val viewModel: AllDealsViewModel by viewModels { AllDealsViewModel.Factory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAllDealsBinding.inflate(inflater, container, false)

        _adapter = createAdapter()

        viewModel.deals.observe(viewLifecycleOwner) { deals ->
            adapter.submitList(deals)
        }

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = layoutManager
        binding.recyclerView.addItemDecoration(DividerItemDecoration(requireContext(), layoutManager.orientation))
        binding.recyclerView.setOnScrollChangeListener { _, _, _, y1, y2 ->
            if (y1 > y2) binding.addButton.hide()
            else binding.addButton.show()
        }


        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
        _binding = null
    }

    private fun createAdapter(): AllDealsAdapter {
        return AllDealsAdapter(
            requireContext(),
            object : ToDoListClickListener {
                override fun onDone(toDoItem: ToDoItem) {
                    viewModel.onDone(toDoItem)
                }

                override fun onChoose(toDoItem: ToDoItem) {
                    viewModel.onChoose(toDoItem)
                }

            }
        )
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        menu.findItem(R.id.viewAll).setOnMenuItemClickListener {
            // TODO: Не робит шо-то
            viewModel.showOrHideDone()
            // if it returns true, no other callbacks will be executed
            true
        }

        super.onCreateOptionsMenu(menu, inflater)
    }
}