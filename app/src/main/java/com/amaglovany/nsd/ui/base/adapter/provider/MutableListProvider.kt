package com.amaglovany.nsd.ui.base.adapter.provider

open class MutableListProvider<T> : ListProvider<T>() {
    protected val mutableList = mutableListOf<T>()
    override val items: List<T> get() = mutableList

    fun add(item: T): Int {
        val position = mutableList.size
        mutableList.add(item)
        return position
    }

    fun remove(item: T): Int {
        val position = mutableList.size
        mutableList.remove(item)
        return position
    }

    fun find(predicate: (T) -> Boolean): T? = items.find(predicate)

    fun removeAt(index: Int) = mutableList.removeAt(index)

    fun addAll(items: Collection<T>, clear: Boolean = false) {
        if (clear) mutableList.clear()
        mutableList.addAll(items)
    }

    fun clear() = mutableList.clear()
}