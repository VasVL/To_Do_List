package com.example.to_dolist.ui.allDealsScreen

import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.util.TypedValueCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.R

class ItemDecoration : RecyclerView.ItemDecoration() {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val last = parent.adapter?.itemCount ?: 0

        // TODO: Ну явно же скруглённые края задаются не через бэкграунд, надо погуглить
        for (view in parent.children) {
            val index = parent.getChildAdapterPosition(view)
            view.background = when (index) {
                0 -> AppCompatResources.getDrawable(parent.context, R.drawable.layout_recycle_item_top)
                last - 1 -> AppCompatResources.getDrawable(parent.context, R.drawable.layout_recycle_item_bottom)
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
            0 -> outRect.top = TypedValueCompat.dpToPx(5f, view.resources.displayMetrics).toInt()
            last - 1 -> {
                outRect.bottom = TypedValueCompat.dpToPx(100f, view.resources.displayMetrics).toInt()
            }
        }
    }
}