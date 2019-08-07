package com.amaglovany.nsd.di.module

import com.amaglovany.nsd.presentation.Contract
import com.amaglovany.nsd.ui.ChatFragment
import toothpick.config.Module

class ChatModule(fragment: ChatFragment) : Module() {
    init {
        bind(Contract.Chat.View::class.java).toInstance(fragment)
    }
}