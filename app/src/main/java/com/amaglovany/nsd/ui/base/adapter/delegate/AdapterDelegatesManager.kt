package com.amaglovany.nsd.ui.base.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

open class AdapterDelegatesManager(private val delegates: List<AdapterDelegate>) {

    fun getItemViewType(position: Int) = delegates.indexOfFirst {
        it.isForViewType(position)
    }

    fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = delegates.elementAtOrElse(viewType, {
        throw IllegalArgumentException("No AdapterDelegates registered for view type: $viewType.")
    }).onCreateViewHolder(parent)

    open fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = delegates
            .elementAtOrNull(holder.itemViewType)?.onBindViewHolder(position, holder) ?: Unit
}