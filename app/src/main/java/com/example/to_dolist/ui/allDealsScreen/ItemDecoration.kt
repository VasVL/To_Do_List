package com.example.to_dolist.ui.allDealsScreen

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.children
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.R
import com.example.to_dolist.data.ToDoItem

class ItemDecoration(
    private val top: Drawable,
    private val bottom: Drawable,
) : RecyclerView.ItemDecoration() {

    private val framePaint = Paint().apply {
        this.isAntiAlias = true
        this.style = Paint.Style.STROKE
        this.strokeWidth = 15.toFloat()
        this.color = Color.parseColor("#000000")
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val last = parent.adapter?.itemCount ?: 0

        // TODO: Ну явно же скруглённые края задаются не через бэкграунд, надо погуглить
        for (view in parent.children) {
            val index = parent.getChildAdapterPosition(view)
            view.background = when (index) {
                last - 1 -> bottom
                0 -> top
                else -> AppCompatResources.getDrawable(parent.context, R.drawable.layout_recycle_item_mid)
            }
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val last = parent.adapter?.itemCount ?: 0
        val index = parent.getChildAdapterPosition(view)
        when (index) {
            last - 1 -> {
                outRect.bottom = 400
            }
        }
    }
}