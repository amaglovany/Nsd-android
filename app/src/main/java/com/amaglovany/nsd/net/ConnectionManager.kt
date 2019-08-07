package com.amaglovany.nsd.net

import android.content.Context
import android.net.nsd.NsdServiceInfo
import com.amaglovany.nsd.net.protocol.Message
import java.net.InetAddress
import javax.inject.Inject

class ConnectionManager @Inject constructor(context: Context) {

    val assistant = NsdAssistant(context)
    var connection: Connection? = null
        private set

    val isDiscovering: Boolean get() = assistant.isDiscovering
    val isServiceCreated: Boolean get() = assistant.isServiceCreated
    val isServerRunning: Boolean get() = connection?.isServerRunning == true
    val isAnyoneConnected: Boolean get() = connection?.isAnyoneConnected == true

    fun create() = Connection().also { connection = it }.create { port ->
        assistant.create(port)
    }

    fun destroy() = connection?.release().also {
        connection = null
        assistant.stopService()
    }

    fun send(address: InetAddress, message: Message) {
        connection?.send(address, message)
    }

    fun connect(info: NsdServiceInfo,
                receiver: MessageReceiver? = null,
                callback: ConnectionCallback? = null
    ) = assistant.connect(info) { connected ->
        connected?.let {
            Connection().also { connection = it }.connect(it.host, it.port, receiver)
            callback?.invoke(it)
        }
    }

    fun disconnect() = connection?.disconnectAll().also { connection = null }

    fun discover() = assistant.discover()

    fun stopDiscovering() = assistant.stopDiscovering()

    fun release() = assistant.release().also {
        connection?.release().also { connection = null }
    }
}