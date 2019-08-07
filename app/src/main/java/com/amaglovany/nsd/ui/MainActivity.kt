package com.amaglovany.nsd.ui

import android.os.Bundle
import com.amaglovany.nsd.R
import com.amaglovany.nsd.di.module.MainModule
import com.amaglovany.nsd.net.ConnectionManager
import com.amaglovany.nsd.ui.base.BaseActivity
import javax.inject.Inject

class MainActivity : BaseActivity() {

    override val fragmentContainerId = R.id.container

    @Inject lateinit var manager: ConnectionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        startFragment(ListFragment(), false)
    }

    override fun createScope() = super.createScope().also {
        it.installModules(MainModule())
    }

    override fun onResume() {
        super.onResume()
        manager.create()
        manager.discover()
    }

    override fun onPause() {
        super.onPause()
        manager.destroy()
        manager.stopDiscovering()
    }

    override fun onDestroy() {
        manager.release()
        super.onDestroy()
    }
}