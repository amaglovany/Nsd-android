package com.amaglovany.nsd.extension

import android.view.View
import java.lang.ref.WeakReference

fun <T> T.weak() = WeakReference(this)
val Any.className: String get() = this.javaClass.simpleName
var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) = changeVisibility(value)

fun View.changeVisibility(visible: Boolean, invisibleState: Int = View.GONE) {
    visibility = if (visible) View.VISIBLE else invisibleState
}