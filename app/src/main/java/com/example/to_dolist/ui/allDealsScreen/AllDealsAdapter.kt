package com.example.to_dolist.ui.allDealsScreen

import android.content.Context
import android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.R
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.databinding.ListItemToDoBinding
import com.example.to_dolist.util.format

interface ToDoListClickListener {
    fun onDone(toDoItem: ToDoItem)

    fun onChoose(toDoItem: ToDoItem)
}

class AllDealsAdapter(
    context: Context,
    private val clickListener: ToDoListClickListener
) : ListAdapter<ToDoItem, AllDealsAdapter.DealViewHolder>(ToDoItemCallback()),
    View.OnClickListener {

    // TODO: мб переделать как-то
    private val colorGray = context.resources.getColor(R.color.gray)
    private val colorBlack = context.resources.getColor(R.color.black)

    override fun onClick(view: View) {
        val tag = view.tag as ToDoItem
        when (view.id) {
            R.id.isDone -> clickListener.onDone(tag)
            else -> clickListener.onChoose(tag)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val binding = ListItemToDoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.isDone.setOnClickListener(this)
        binding.root.setOnClickListener(this)

        return DealViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        holder.onBind(getItem(position), colorGray, colorBlack)
    }

    class DealViewHolder(private val binding: ListItemToDoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: ToDoItem, colorGray: Int, colorBlack: Int) {
            this.itemView.tag = item
            with(binding) {
                isDone.tag = item
                isDone.isChecked = item.isDone
                if (item.isDone) {
                    // TODO: Здесь кстати датабиндинг бог бы помочь
                    //  В любом случае лучше переделать на два разных Item'а
                    deal.setTextColor(colorGray)
                    deal.text = "${item.deal}"
                    deal.paintFlags = STRIKE_THRU_TEXT_FLAG
                } else {
                    deal.text = item.getDealWithImportance()
                    deal.setTextColor(colorBlack)
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
}