package com.example.to_dolist.ui.allDealsScreen

import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.R
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.databinding.ListItemToDoBinding
import com.example.to_dolist.util.format
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface ToDoListClickListener {
    fun onDone(toDoItem: ToDoItem)

//    fun onChoose(toDoItem: ToDoItem)
    fun onChoose(view: View)
}



class AllDealsAdapter(
    private val clickListener: ToDoListClickListener
) : ListAdapter<ToDoItem, AllDealsAdapter.DealViewHolder>(ToDoItemCallback()),
    View.OnClickListener {

    fun filterAndSubmitList(list: List<ToDoItem>, isDoneShowed: Boolean) {
        CoroutineScope(SupervisorJob() + Dispatchers.Default).launch {
            val newList = if (isDoneShowed) {
                list.sortedBy { it.deadline }
            } else {
                list.filter { !it.isDone }.sortedBy { it.deadline }
            }
            withContext(Dispatchers.Main) {

                submitList(newList)
            }
        }
    }

    override fun onClick(view: View) {
        val tag = view.tag as ItemTag
        when (view.id) {
            R.id.isDone -> clickListener.onDone(tag.item)
            else -> clickListener.onChoose(view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val binding = ListItemToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.isDone.setOnClickListener(this)
        binding.root.setOnClickListener(this)

        return DealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class DealViewHolder(private val binding: ListItemToDoBinding) : RecyclerView.ViewHolder(binding.root) {

        val item = binding.item
        val root = binding.root
        fun onBind(item: ToDoItem) {
            /** туть!!! */ ViewCompat.setTransitionName(binding.root, "${item.id}") /** туть!!! */

            this.itemView.tag = ItemTag(item)
            with(binding) {
                isDone.tag = ItemTag(item)
                isDone.isChecked = item.isDone
                if (item.isDone) {
                    val greyColor = deal.context.resources.getColor(R.color.gray)
                    deal.setTextColor(greyColor)
                    deal.text = "${item.text}"
                    deal.paintFlags = STRIKE_THRU_TEXT_FLAG
                } else {
                    val blackColor = deal.context.resources.getColor(R.color.black)
                    deal.setTextColor(blackColor)
                    deal.text = item.getDealWithImportance()
                    deal.paintFlags = 0
                }
                deadline.text = item.deadline?.format() ?: ""
            }
        }
    }

    class ToDoItemCallback : DiffUtil.ItemCallback<ToDoItem>() {
        override fun areItemsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ToDoItem, newItem: ToDoItem): Boolean {
            return oldItem == newItem
        }
    }

    class ItemTag(val item: ToDoItem, var isNotSwiping: Boolean = true)
}

