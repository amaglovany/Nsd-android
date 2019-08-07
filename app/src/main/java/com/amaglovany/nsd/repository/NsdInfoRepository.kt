package com.amaglovany.nsd.repository

import android.net.nsd.NsdServiceInfo
import com.amaglovany.nsd.net.NsdAssistant
import com.amaglovany.nsd.ui.base.adapter.provider.MutableListProvider
import javax.inject.Inject

class NsdInfoRepository @Inject constructor(private val assistant: NsdAssistant)
    : Repository<MutableListProvider<NsdServiceInfo>>(), NsdAssistant.Listener {

    override val provider = MutableListProvider<NsdServiceInfo>()
    override val isEmpty: Boolean get() = provider.itemCount == 0

    val current: NsdServiceInfo? get() = assistant.current

    init {
        assistant.listener = this
    }

    override fun onServiceRegistered() = notifyDataChanged(0)
    override fun onServiceUnregistered() = onServiceRegistered()

    private fun find(info: NsdServiceInfo): NsdServiceInfo?
            = provider.find { it.serviceName == info.serviceName }

    override fun onServiceFound(info: NsdServiceInfo) {
        find(info) ?: notifyItemInserted(provider.add(info))
    }

    override fun onServiceLost(info: NsdServiceInfo) {
        notifyItemRemoved(provider.remove(find(info) ?: return))
    }
}