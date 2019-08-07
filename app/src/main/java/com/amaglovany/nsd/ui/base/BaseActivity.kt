package com.amaglovany.nsd.ui.base

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import toothpick.Scope
import toothpick.Toothpick
import toothpick.smoothie.module.SmoothieSupportActivityModule

abstract class BaseActivity : AppCompatActivity() {

    open protected val fragmentContainerId = 0

    private lateinit var scope: Scope

    fun startFragment(fragment: Fragment, addToBackStack: Boolean): Boolean = try {
        startFragmentSimple(fragmentContainerId, fragment, addToBackStack)
    } catch (e: Exception) {
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        scope = createScope()
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onDestroy() {
        Toothpick.closeScope(this)
        super.onDestroy()
    }

    open protected fun createScope(): Scope = Toothpick.openScopes(application, this).also {
        it.installModules(SmoothieSupportActivityModule(this))
    }

    protected fun startFragmentSimple(containerId: Int, fragment: Fragment, addToBackStack: Boolean): Boolean {
        val manager = supportFragmentManager
        if (!addToBackStack) manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        val transaction = manager.beginTransaction()
        val tag = fragment::class.java.simpleName
        if (addToBackStack) {
            transaction.addToBackStack(tag)
        }

        transaction
                .replace(containerId, fragment, tag)
                .commit()

        return true
    }
}