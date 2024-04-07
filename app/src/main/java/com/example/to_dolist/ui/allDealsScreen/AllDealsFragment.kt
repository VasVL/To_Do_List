package com.example.to_dolist.ui.allDealsScreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.OnApplyWindowInsetsListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.to_dolist.R
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.databinding.FragmentAllDealsBinding
import com.google.android.material.appbar.AppBarLayout

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        /** туть!!! */ postponeEnterTransition() /** туть!!! */

        /** ------------------ Обсерверы ------------------ */


        viewModel.deals.observe(viewLifecycleOwner) { deals ->
            adapter.submitList(deals)
            /** туть!!! */ startPostponedEnterTransition() /** туть!!! */
        }

        viewModel.isDoneShowed.observe(viewLifecycleOwner) { isDoneShowed ->
            if (isDoneShowed) binding.showAllDeals.setImageResource(R.drawable.baseline_visibility_off_24)
            else binding.showAllDeals.setImageResource(R.drawable.baseline_visibility_24)
        }

        viewModel.doneCount.observe(viewLifecycleOwner) { count ->
            count?.let{ binding.textDone.text = "Выполнено - $count" }
        }

        // TODO: Баг - если пометить как выполненный элемент из начала списка, а потом нажать "показать выполненные",
        //  то CollapsingLayout всё равно остаётся развёрнут, а первый элемент скрыт под ним


        /** ------------------ binding ------------------ */


        val myLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        with(binding) {
            recyclerView.adapter = adapter
            recyclerView.layoutManager = myLayoutManager
            recyclerView.addItemDecoration(ItemDecoration())
            recyclerView.setOnScrollChangeListener(hideOrShowFABListener)

            // Вручную считаем и рисуем тень под тулбаром
            appBar.addOnOffsetChangedListener(toolbarShowShadowListener)

            /** Это, чтоб SystemBar не перкрывал fab
             * Только на моём телефоне не работает чё-то... */
            ViewCompat.setOnApplyWindowInsetsListener(addButton, onApplyWindowInsetsListener)

            addButton.setOnClickListener {
                val directions = AllDealsFragmentDirections.actionAllDealsFragmentToChangeDealFragment(ToDoItem())
                findNavController().navigate(directions)
            }

            showAllDeals.setOnClickListener {
                viewModel.showOrHideDone()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _adapter = null
        _binding = null
    }


    private val onApplyWindowInsetsListener = OnApplyWindowInsetsListener { v, windowInsets ->

        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())

        v.updateLayoutParams<ViewGroup.MarginLayoutParams> {
            bottomMargin = insets.bottom
            leftMargin = insets.left
            rightMargin = insets.right
        }

        WindowInsetsCompat.CONSUMED
    }


    private val toolbarShowShadowListener = AppBarLayout.OnOffsetChangedListener { appBarLayout, offset ->
        val collapsingToolbar = binding.collapsingToolbar.height
        val toolbar = binding.toolbar.height
        val insets = ViewCompat.getRootWindowInsets(appBarLayout)?.getInsets(WindowInsetsCompat.Type.systemBars())
        val size = insets?.top ?: 0
        binding.toolbarShadow.visibility = if (collapsingToolbar + offset == size + toolbar) {
            View.VISIBLE
        } else {
            View.INVISIBLE
        }

    }


    private val hideOrShowFABListener = View.OnScrollChangeListener { _, _, y1, _, y2 ->
        if (y1 > y2) {
            binding.addButton.hide()
        } else {
            binding.addButton.show()
        }
    }


    private fun createAdapter(): AllDealsAdapter {
        return AllDealsAdapter(
            object : ToDoListClickListener {
                override fun onDone(toDoItem: ToDoItem) {
                    viewModel.onDone(toDoItem)
                }

                override fun onChoose(view: View) {
                    /** туть!!! */ val extras = FragmentNavigatorExtras(view to "2") /** туть!!! */
//                    val directions = AllDealsFragmentDirections.actionAllDealsFragmentToChangeDealFragment(toDoItem)
                    val directions = AllDealsFragmentDirections.actionAllDealsFragmentToChangeDealFragment(view.tag as ToDoItem)
                    findNavController().navigate(directions, extras)
                }

            }
        )
    }
}