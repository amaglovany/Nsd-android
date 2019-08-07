package com.amaglovany.nsd.presentation

import com.amaglovany.nsd.extension.weak

abstract class Presenter<out V : PresentationView>(view: V) {
    private val reference = view.weak()
    protected val view: V? get() = reference.get()
}