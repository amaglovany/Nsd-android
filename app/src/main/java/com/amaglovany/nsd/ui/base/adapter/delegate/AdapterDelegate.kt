package com.amaglovany.nsd.ui.base.adapter.delegate

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup

interface AdapterDelegate {
    fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder
    fun onBindViewHolder(position: Int, holder: RecyclerView.ViewHolder)
    fun isForViewType(position: Int): Boolean
}