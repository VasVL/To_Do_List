package com.example.to_dolist.ui.allDealsScreen

import android.graphics.Canvas
import android.graphics.Outline
import android.os.Bundle
import android.util.Log
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
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.ACTION_STATE_SWIPE
import androidx.recyclerview.widget.ItemTouchHelper.END
import androidx.recyclerview.widget.ItemTouchHelper.START
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.R
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.databinding.FragmentAllDealsBinding
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.sign

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

            /** Swipe */
            val touchHelper = ItemTouchHelper(ItemTouchHelperCallback())
            touchHelper.attachToRecyclerView(recyclerView)


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
                    val directions = AllDealsFragmentDirections.actionAllDealsFragmentToChangeDealFragment((view.tag as AllDealsAdapter.ItemTag).item)
                    findNavController().navigate(directions, extras)
                }

            }
        )
    }

    private inner class ItemTouchHelperCallback : ItemTouchHelper.Callback() {
        override fun isItemViewSwipeEnabled(): Boolean {
            return true
        }

        override fun isLongPressDragEnabled(): Boolean {
            return false
        }

        override fun getMovementFlags(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder
        ): Int {
            val dragFlags = 0//ItemTouchHelper.UP or ItemTouchHelper.DOWN
            val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
            return makeMovementFlags(dragFlags, swipeFlags)
        }

        // Never be called
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val index = viewHolder.adapterPosition
            val item = viewModel.deals.value?.get(index)
            if (item != null) {
                when (direction) {
                    START -> viewModel.onDelete(item)
                    END -> viewModel.onDone(item)
                }
            }
        }


        override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
            if (viewHolder == null) return

            if (actionState == ACTION_STATE_SWIPE) {
                (viewHolder.itemView.tag as AllDealsAdapter.ItemTag).isSwipeStart = true
                (viewHolder.itemView.tag as AllDealsAdapter.ItemTag).isSwipeEnd = false

                getDefaultUIUtil().onSelected((viewHolder as AllDealsAdapter.DealViewHolder).item)
            } else {
                super.onSelectedChanged(viewHolder, actionState)
            }
        }

        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
           getDefaultUIUtil().clearView((viewHolder as AllDealsAdapter.DealViewHolder?)?.item)
            (viewHolder.itemView.tag as AllDealsAdapter.ItemTag).isSwipeEnd = true
//            super.clearView(recyclerView, viewHolder)
        }

        override fun onChildDraw(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            if (actionState != ACTION_STATE_SWIPE) {
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                return
            }

            if ((viewHolder.itemView.tag as AllDealsAdapter.ItemTag).isSwipeStart) {
                when (dX.sign) {
                    -1.0f -> {
                        (viewHolder as AllDealsAdapter.DealViewHolder).root.background = resources.getDrawable(R.drawable.swipe_delete_background)
                        (viewHolder.itemView.tag as AllDealsAdapter.ItemTag).isSwipeStart = false
                    }
                    1.0f -> {
                        (viewHolder as AllDealsAdapter.DealViewHolder).root.background = resources.getDrawable(R.drawable.swipe_done_background)
                        (viewHolder.itemView.tag as AllDealsAdapter.ItemTag).isSwipeStart = false
                    }
                }
            }
            getDefaultUIUtil().onDraw(c, recyclerView, (viewHolder as AllDealsAdapter.DealViewHolder?)?.item, dX, dY, actionState, isCurrentlyActive)
        }

        override fun onChildDrawOver(
            c: Canvas,
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder?,
            dX: Float,
            dY: Float,
            actionState: Int,
            isCurrentlyActive: Boolean
        ) {
            if (actionState == ACTION_STATE_SWIPE) {
                getDefaultUIUtil().onDrawOver(c, recyclerView, (viewHolder as AllDealsAdapter.DealViewHolder?)?.item, dX, dY, actionState, isCurrentlyActive)
            } else {
                super.onChildDrawOver(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }
    }
}