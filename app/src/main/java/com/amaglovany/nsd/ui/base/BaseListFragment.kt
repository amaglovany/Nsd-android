package com.amaglovany.nsd.ui.base

import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.View
import com.amaglovany.nsd.R
import com.amaglovany.nsd.presentation.UpdatableView
import com.amaglovany.nsd.ui.base.adapter.RecyclerAdapter
import com.amaglovany.nsd.ui.base.view.ViewHolder
import kotlinx.android.synthetic.main.fragment_chat.view.*

abstract class BaseListFragment : BaseFragment(), UpdatableView, ViewHolder.OnViewHolderClickListener {

    override val layoutResId = R.layout.fragment_list

    protected lateinit var adapter: RecyclerAdapter

    abstract protected fun createAdapter(): RecyclerAdapter
    abstract protected fun configure(recyclerView: RecyclerView)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = createAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.recyclerView.apply {
            adapter = this@BaseListFragment.adapter
            configure(this)
        }
    }

    override fun onViewHolderClick(viewHolder: RecyclerView.ViewHolder, position: Int) = Unit

    override fun update() = adapter.notifyDataSetChanged()
    override fun onItemInserted(position: Int) = adapter.notifyItemInserted(position)
    override fun onItemRemoved(position: Int) = adapter.notifyItemRemoved(position)
}