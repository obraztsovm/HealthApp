package com.example.healthapp.ui.helpers

import android.graphics.Canvas
import android.graphics.Color
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.healthapp.ui.adapters.HealthRecordAdapter

class SwipeToDeleteCallback(
    private val adapter: HealthRecordAdapter,
    private val onDelete: (position: Int) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        onDelete(position)
    }

    override fun onChildDraw(
        canvas: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        // Простой красный фон при свайпе
        val itemView = viewHolder.itemView
        if (dX > 0) {
            // Свайп вправо
            itemView.setBackgroundColor(Color.RED)
        } else {
            // Свайп влево
            itemView.setBackgroundColor(Color.RED)
        }

        super.onChildDraw(canvas, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        // Восстанавливаем нормальный фон
        viewHolder.itemView.setBackgroundColor(Color.WHITE)
    }
}