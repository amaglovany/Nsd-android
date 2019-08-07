package com.amaglovany.nsd.di.module

import android.content.Context
import com.amaglovany.nsd.net.ConnectionManager
import com.amaglovany.nsd.net.NsdAssistant
import toothpick.config.Module

class AppModule(context: Context) : Module() {
    init {
        bind(Context::class.java).toInstance(context)

        val manager = ConnectionManager(context)
        bind(ConnectionManager::class.java).toInstance(manager)
        bind(NsdAssistant::class.java).toInstance(manager.assistant)
    }
}