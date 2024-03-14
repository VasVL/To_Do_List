package com.example.to_dolist.ui.allDealsScreen

import android.icu.util.Calendar
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.data.ToDoItem
import com.example.to_dolist.databinding.ListItemToDoBinding
import com.example.to_dolist.util.format

class AllDealsAdapter : RecyclerView.Adapter<AllDealsAdapter.DealViewHolder>() {

    var deals = listOf<ToDoItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DealViewHolder {
        val binding = ListItemToDoBinding.inflate(LayoutInflater.from(parent.context))
        return DealViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return deals.size
    }

    override fun onBindViewHolder(holder: DealViewHolder, position: Int) {
        holder.onBind(deals[position])
    }

    class DealViewHolder(private val binding: ListItemToDoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: ToDoItem) {
            with(binding) {
                isDone.isChecked = item.isDone
                deal.text = item.deal
                deadline.text = item.deadline.format()
            }
        }
    }
}