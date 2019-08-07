package com.amaglovany.nsd.di.module

import com.amaglovany.nsd.presentation.Contract
import com.amaglovany.nsd.presentation.ListPresenter
import com.amaglovany.nsd.ui.ListFragment
import toothpick.config.Module

class ListModule(fragment: ListFragment) : Module() {
    init {
        bind(Contract.List.View::class.java).toInstance(fragment)
        bind(Contract.List.Presenter::class.java).to(ListPresenter::class.java)
    }
}