package com.amaglovany.nsd.ui

import android.net.nsd.NsdServiceInfo
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.amaglovany.nsd.R
import com.amaglovany.nsd.extension.isVisible
import com.amaglovany.nsd.model.Wrapper
import com.amaglovany.nsd.net.ConnectionManager
import com.amaglovany.nsd.net.MessageReceiver
import com.amaglovany.nsd.net.protocol.Greeting
import com.amaglovany.nsd.presentation.Contract
import com.amaglovany.nsd.ui.base.BaseListFragment
import com.amaglovany.nsd.ui.base.adapter.RecyclerAdapter
import com.amaglovany.nsd.ui.base.adapter.provider.MutableListProvider
import com.amaglovany.nsd.ui.delegate.MessageAdapterDelegate
import kotlinx.android.synthetic.main.fragment_chat.*

class ChatFragment : BaseListFragment(), View.OnClickListener, Contract.Chat.View, MessageReceiver {

    companion object {
        private const val EXTRA = "EXTRA"//info

        fun newInstance(info: NsdServiceInfo) = ChatFragment().apply {
            arguments = Bundle(1).also {
                it.putParcelable(EXTRA, info)
            }
        }
    }

    override val layoutResId = R.layout.fragment_chat
    override val toolbarTitle: CharSequence? get() = info.serviceName

    private val provider = MutableListProvider<Wrapper>()
    private val manager: ConnectionManager get() = (activity as MainActivity).manager

    private lateinit var info: NsdServiceInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        info = arguments.getParcelable(EXTRA) ?: return
        manager.connect(info, this) {
            it?.let {
                info = it
                arguments.putParcelable(EXTRA, info)
            } ?: onBackPressed()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonSend.setOnClickListener(this)
        editText.addTextChangedListener(watcher)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.buttonSend -> {
                manager.send(info.host, Greeting(editText.text.toString()))
                editText.text = null
                recyclerView.scrollToPosition(provider.itemCount - 1)
            }
        }
    }

    override fun onMessageReceived(message: Wrapper) {
        provider.add(message)
        recyclerView.scrollToPosition(provider.itemCount - 1)
    }

    override fun onPause() {
        super.onPause()
        manager.disconnect()
    }

    override fun createAdapter() = RecyclerAdapter(listOf(MessageAdapterDelegate(provider)), provider)

    override fun configure(recyclerView: RecyclerView) {
        recyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false).also {
            it.stackFromEnd = true
        }
    }

    private val watcher = object : TextWatcher {
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            buttonSend.isVisible = !s.isNullOrEmpty()
        }

        override fun afterTextChanged(s: Editable?) = Unit
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
    }
}