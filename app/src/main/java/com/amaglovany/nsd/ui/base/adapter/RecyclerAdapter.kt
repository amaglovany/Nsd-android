package com.amaglovany.nsd.ui.base.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.amaglovany.nsd.ui.base.adapter.delegate.AdapterDelegate
import com.amaglovany.nsd.ui.base.adapter.delegate.AdapterDelegatesManager
import com.amaglovany.nsd.ui.base.adapter.provider.Provider

open class RecyclerAdapter(
        delegates: List<AdapterDelegate>,
        private val provider: Provider
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    open val manager = AdapterDelegatesManager(delegates)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = manager.onCreateViewHolder(parent, viewType)

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int)
            = manager.onBindViewHolder(holder, position)

    override fun getItemViewType(position: Int) = manager.getItemViewType(position)

    override fun getItemCount() = provider.itemCount
}