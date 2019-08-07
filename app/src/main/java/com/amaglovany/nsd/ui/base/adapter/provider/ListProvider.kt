package com.amaglovany.nsd.ui.base.adapter.provider

abstract class ListProvider<out T> : Provider {
    abstract val items: List<T>
    override val itemCount: Int get() = items.size
}