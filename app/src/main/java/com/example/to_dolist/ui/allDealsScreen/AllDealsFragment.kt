package com.example.to_dolist.ui.allDealsScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_dolist.R
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.databinding.FragmentAllDealsBinding

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

        viewModel.isDoneShowed.observe(viewLifecycleOwner) { isDoneShowed ->
            if (isDoneShowed) {
                binding.showAllDeals.setImageResource(R.drawable.baseline_visibility_off_24)
            }
            else {
                binding.showAllDeals.setImageResource(R.drawable.baseline_visibility_24)
            }
        }

        viewModel.doneCount.observe(viewLifecycleOwner) { count ->
            count?.let{
                binding.textDone.text = "Выполнено - $count"
            }
        }



        val myLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = myLayoutManager
            recyclerView.addItemDecoration(ItemDecoration())
            recyclerView.setOnScrollChangeListener { v, _, y1, _, y2 ->
                if (y1 > y2) {
                    binding.addButton.hide()
                } else {
                    binding.addButton.show()
                }
            }

            appBar.addOnOffsetChangedListener { appBarLayout, offset ->
                val collapsingToolbar = collapsingToolbar.height
                val toolbar = toolbar.height
                val insets = ViewCompat.getRootWindowInsets(appBarLayout)?.getInsets(WindowInsetsCompat.Type.systemBars())
                val size = insets?.top ?: 0
                if (collapsingToolbar + offset == size + toolbar) {
                    toolbarShadow.visibility = View.VISIBLE
                } else {
                    toolbarShadow.visibility = View.INVISIBLE
                }
            }

            /** Это, чтоб SystemBar не перкрывал fab
             * Только на моём телефоне не работает чё-то... */
            ViewCompat.setOnApplyWindowInsetsListener(addButton) { v, windowInsets ->
                val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

                v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                    bottomMargin = insets.bottom
                    leftMargin = insets.left
                    rightMargin = insets.right
                }

                WindowInsetsCompat.CONSUMED
            }

            showAllDeals.setOnClickListener {
                viewModel.showOrHideDone()
            }
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
            object : ToDoListClickListener {
                override fun onDone(toDoItem: ToDoItem) {
                    viewModel.onDone(toDoItem)
                }

                override fun onChoose(toDoItem: ToDoItem) {
                    viewModel.onChoose(toDoItem)
                    val directions = AllDealsFragmentDirections.actionAllDealsFragmentToChangeDealFragment(toDoItem.id)
                    findNavController().navigate(directions)
                }

            }
        )
    }
}