package com.amaglovany.nsd.ui.delegate

import android.net.nsd.NsdServiceInfo
import android.view.ViewGroup
import com.amaglovany.nsd.ui.base.adapter.delegate.ListProviderDelegate
import com.amaglovany.nsd.ui.base.adapter.provider.ListProvider
import com.amaglovany.nsd.ui.base.view.ViewHolder
import com.amaglovany.nsd.ui.holder.InfoViewHolder

class InfoAdapterDelegate(
        provider: ListProvider<NsdServiceInfo>,
        listener: ViewHolder.OnViewHolderClickListener?
) : ListProviderDelegate<NsdServiceInfo, ViewHolder.OnViewHolderClickListener, InfoViewHolder>(provider, listener) {

    override fun onCreateViewHolder(parent: ViewGroup) = InfoViewHolder(parent, listener)

    override fun onBindViewHolder(position: Int, holder: InfoViewHolder, provider: ListProvider<NsdServiceInfo>, item: NsdServiceInfo) {
        holder.textView.text = item.serviceName
    }
}