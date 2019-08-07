package com.amaglovany.nsd.ui.base

import android.os.Bundle
import android.support.annotation.CallSuper
import android.support.v4.app.Fragment
import android.support.v7.app.ActionBar
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import toothpick.Scope
import toothpick.Toothpick


abstract class BaseFragment : Fragment() {

    abstract val layoutResId: Int

    protected lateinit var scope: Scope

    open protected val toolbarTitleRes = 0
    open protected val toolbarIndicatorRes = 0
    open protected val toolbarTitle: CharSequence?
        get() = toolbarTitleRes.let {
            return@let if (it != 0) getText(it) else null
        }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(layoutResId, container, false)

    override fun onCreate(savedInstanceState: Bundle?) {
        scope = createScope()
        super.onCreate(savedInstanceState)
        Toothpick.inject(this, scope)
    }

    override fun onStart() {
        super.onStart()
        ((context as? AppCompatActivity)?.supportActionBar)?.let { onActionBarReady(it) }
    }

    override fun onDestroy() {
        Toothpick.closeScope(scope)
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem) = if (item.itemId == android.R.id.home) {
        onBackPressed()
        true

    } else super.onOptionsItemSelected(item)

    open protected fun createScope(): Scope = Toothpick.openScopes(activity, this)

    @CallSuper
    open protected fun onActionBarReady(bar: ActionBar) {
        bar.title = toolbarTitle

        if (toolbarIndicatorRes == 0) {
            bar.setDisplayHomeAsUpEnabled(false)
            bar.setDisplayShowHomeEnabled(false)
        } else {
            bar.setDisplayHomeAsUpEnabled(true)
            bar.setDisplayShowHomeEnabled(true)
            bar.setHomeAsUpIndicator(toolbarIndicatorRes)
        }
    }

    open fun startFragment(fragment: BaseFragment, addToBackStack: Boolean = true) {
        (context as? BaseActivity)?.startFragment(fragment, addToBackStack)
    }

    open fun onBackPressed() {
        (context as? BaseActivity)?.onBackPressed()
    }
}