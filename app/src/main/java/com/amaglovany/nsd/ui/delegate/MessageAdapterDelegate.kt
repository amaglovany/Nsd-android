package com.amaglovany.nsd.ui.delegate

import android.view.ViewGroup
import com.amaglovany.nsd.model.Wrapper
import com.amaglovany.nsd.ui.base.adapter.delegate.ListProviderDelegate
import com.amaglovany.nsd.ui.base.adapter.provider.ListProvider
import com.amaglovany.nsd.ui.base.view.ViewHolder
import com.amaglovany.nsd.ui.holder.MessageViewHolder

class MessageAdapterDelegate(
        provider: ListProvider<Wrapper>
) : ListProviderDelegate<Wrapper, ViewHolder.OnViewHolderClickListener, MessageViewHolder>(provider, null) {

    override fun onCreateViewHolder(parent: ViewGroup) = MessageViewHolder(parent)

    override fun onBindViewHolder(position: Int, holder: MessageViewHolder, provider: ListProvider<Wrapper>, item: Wrapper) {
        holder.textView.text = item.body
        holder.alignEnd = item.our
    }
}