package com.amaglovany.nsd.ui.base.adapter.delegate

import android.support.v7.widget.RecyclerView
import com.amaglovany.nsd.ui.base.view.ViewHolder
import com.amaglovany.nsd.extension.weak
import java.lang.ref.WeakReference

abstract class ProviderDelegate<L, in V : ViewHolder<L>, P>(
        val provider: P,
        listener: L? = null
) : AdapterDelegate {

    protected val listener: WeakReference<L>? = listener?.weak()

    @Suppress("UNCHECKED_CAST")
    override final fun onBindViewHolder(position: Int, holder: RecyclerView.ViewHolder) {
        onBindViewHolder(position, holder as? V ?: return, provider)
    }

    abstract fun onBindViewHolder(position: Int, holder: V, provider: P)

    override final fun isForViewType(position: Int): Boolean {
        return isForViewType(position, provider)
    }

    open fun isForViewType(position: Int, provider: P): Boolean = true
}