package com.amaglovany.nsd.ui

import android.support.v7.app.ActionBar
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.amaglovany.nsd.R
import com.amaglovany.nsd.di.module.ListModule
import com.amaglovany.nsd.presentation.Contract
import com.amaglovany.nsd.ui.base.BaseListFragment
import com.amaglovany.nsd.ui.base.adapter.RecyclerAdapter
import com.amaglovany.nsd.ui.base.view.ViewHolder
import com.amaglovany.nsd.ui.delegate.InfoAdapterDelegate
import javax.inject.Inject

class ListFragment : BaseListFragment(), Contract.List.View, ViewHolder.OnViewHolderClickListener {

    override val layoutResId = R.layout.fragment_list
    override val toolbarTitleRes = R.string.app_name

    private lateinit var actionBar: ActionBar
    @Inject lateinit var presenter: Contract.List.Presenter

    private var paused = false

    override fun createScope() = super.createScope().also {
        it.installModules(ListModule(this))
    }

    override fun onActionBarReady(bar: ActionBar) {
        super.onActionBarReady(bar)
        actionBar = bar
    }

    override fun onViewHolderClick(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val info = presenter.get(position) ?: return
        startFragment(ChatFragment.newInstance(info), true)
    }

    override fun setSubtitle(text: String?) {
        if (!paused) actionBar.subtitle = text
    }

    override fun onResume() {
        super.onResume()
        paused = false
    }

    override fun onPause() {
        super.onPause()
        paused = true
    }

    override fun createAdapter(): RecyclerAdapter {
        val repository = presenter.repository
        return RecyclerAdapter(listOf(InfoAdapterDelegate(repository.provider, this)), repository.provider)
    }

    override fun configure(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
    }
}