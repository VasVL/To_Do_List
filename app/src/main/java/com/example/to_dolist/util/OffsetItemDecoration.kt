package com.example.to_dolist.util

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView

class OffsetItemDecoration(
    private val leftOffset: Int = 0,
    private val topOffset: Int = 0,
    private val rightOffset: Int = 0,
    private val bottomOffset: Int = 0,
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        outRect.set(leftOffset, topOffset, rightOffset, bottomOffset)
    }
}