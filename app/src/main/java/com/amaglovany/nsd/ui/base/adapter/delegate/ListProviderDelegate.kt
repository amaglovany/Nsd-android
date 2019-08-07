package com.amaglovany.nsd.ui.base.adapter.delegate

import com.amaglovany.nsd.ui.base.adapter.provider.ListProvider
import com.amaglovany.nsd.ui.base.view.ViewHolder

abstract class ListProviderDelegate<T, L, in V : ViewHolder<L>>(
        provider: ListProvider<T>,
        listener: L?
) : ProviderDelegate<L, V, ListProvider<T>>(provider, listener) {

    override fun onBindViewHolder(position: Int, holder: V, provider: ListProvider<T>) =
        onBindViewHolder(position, holder, provider, provider.items[position])

    abstract fun onBindViewHolder(position: Int, holder: V, provider: ListProvider<T>, item: T)
    override fun isForViewType(position: Int, provider: ListProvider<T>) = provider.itemCount != 0
}