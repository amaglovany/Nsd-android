package com.amaglovany.nsd.ui.base.view

import android.content.Context
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import java.lang.ref.WeakReference

abstract class ViewHolder<L>(
        parent: ViewGroup,
        protected val listener: WeakReference<L>?,
        @LayoutRes layoutResId: Int,
        clickable: Boolean = false
) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(layoutResId, parent, false)) {

    val context: Context get() = itemView.context

    init {
        if (clickable && listener?.get() is OnViewHolderClickListener) {
            itemView.setOnClickListener {
                (listener.get() as? OnViewHolderClickListener)?.onViewHolderClick(this, adapterPosition)
            }
        }
    }

    interface OnViewHolderClickListener {
        fun onViewHolderClick(viewHolder: RecyclerView.ViewHolder, position: Int)
    }
}