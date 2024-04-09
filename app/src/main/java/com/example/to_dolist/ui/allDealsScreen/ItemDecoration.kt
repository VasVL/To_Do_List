package com.example.to_dolist.ui.allDealsScreen

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.util.TypedValueCompat
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.to_dolist.R

class ItemDecoration : RecyclerView.ItemDecoration() {

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val last = parent.adapter?.itemCount ?: 0

        if (last == 1) { //один элемент: срезать края и там и там
            parent.children.first().background = AppCompatResources.getDrawable(parent.context, R.drawable.layout_recycle_item_single)
            (parent.children.first() as ViewGroup).children.first().background = AppCompatResources.getDrawable(parent.context, R.drawable.layout_recycle_item_single)
            return
        }

        // TODO: Ну явно же скруглённые края задаются не через бэкграунд, надо погуглить
        for (view in parent.children) {
            val index = parent.getChildAdapterPosition(view)
            (view as ViewGroup).children.first().background = when (index) {
                0 -> {
                    if ((view.tag as AllDealsAdapter.ItemTag).isSwipeEnd) {
                        view.background = AppCompatResources.getDrawable(parent.context, R.drawable.layout_recycle_item_top)
                    }
                    AppCompatResources.getDrawable(parent.context, R.drawable.layout_recycle_item_top)
                }
                last - 1 -> {
                    if ((view.tag as AllDealsAdapter.ItemTag).isSwipeEnd) {
                        view.background = AppCompatResources.getDrawable(
                            parent.context,
                            R.drawable.layout_recycle_item_bottom
                        )
                    }
                    AppCompatResources.getDrawable(parent.context, R.drawable.layout_recycle_item_bottom)
                }
                else -> {
                    if ((view.tag as AllDealsAdapter.ItemTag).isSwipeEnd) {
                        view.background = AppCompatResources.getDrawable(
                            parent.context,
                            R.drawable.layout_recycle_item_mid
                        )
                    }
                    AppCompatResources.getDrawable(parent.context, R.drawable.layout_recycle_item_mid)
                }
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
            0 -> outRect.top = TypedValueCompat.dpToPx(2f, view.resources.displayMetrics).toInt()
            last - 1 -> {
                // TODO: Багулина при пометке выполненным последнего эдемента
                //  Все баги с последим элементом из-за того, что пересчитываются только элементы после изменившегося
                outRect.bottom = TypedValueCompat.dpToPx(100f, view.resources.displayMetrics).toInt()
            }
        }
    }


}