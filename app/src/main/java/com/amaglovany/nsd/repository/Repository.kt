package com.amaglovany.nsd.repository

import java.util.*

abstract class Repository<out PROVIDER> {

    interface Callback {
        fun onDataChanged(payload: Int)
        fun onItemInserted(position: Int)
        fun onItemRemoved(position: Int)
    }

    abstract val provider: PROVIDER
    abstract val isEmpty: Boolean

    private val subscribers: WeakHashMap<Callback, Boolean> = WeakHashMap(1)

    fun subscribe(callback: Callback) = subscribers.put(callback, true)
    fun unSubscribe(callback: Callback) = subscribers.remove(callback)

    protected fun notifyDataChanged(payload: Int) = subscribers.keys.forEach { it.onDataChanged(payload) }
    protected fun notifyItemInserted(position: Int) = subscribers.keys.forEach { it.onItemInserted(position) }
    protected fun notifyItemRemoved(position: Int) = subscribers.keys.forEach { it.onItemRemoved(position) }
}