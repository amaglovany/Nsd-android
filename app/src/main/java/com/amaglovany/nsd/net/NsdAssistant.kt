package com.amaglovany.nsd.net

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Handler
import android.os.Looper
import android.util.Log

class NsdAssistant(context: Context) {

    interface Listener {
        fun onServiceRegistered()
        fun onServiceUnregistered()

        fun onServiceFound(info: NsdServiceInfo)
        fun onServiceLost(info: NsdServiceInfo)
    }

    companion object {
        const val TAG = "ASST"

        const val SERVICE_NAME = "AirDroid"
        const val SERVICE_TYPE = "_test._tcp."
    }

    val isDiscovering: Boolean get() = discoveryListener != null
    val isServiceCreated: Boolean get() = registrationListener != null

    var listener: Listener? = null
    private var connectionCallback: ConnectionCallback? = null

    private val manager = context.getSystemService(Context.NSD_SERVICE) as NsdManager

    private var registrationListener: NsdManager.RegistrationListener? = null
    private var discoveryListener: NsdManager.DiscoveryListener? = null

    private val resolveListener = initializeResolveListener()
    private val handler = Handler(Looper.getMainLooper())

    var current: NsdServiceInfo? = null

    fun create(port: Int) {
        registrationListener ?: initializeRegistrationListener().also { registrationListener = it }

        manager.registerService(NsdServiceInfo().apply {
            this.port = port
            serviceName = SERVICE_NAME
            serviceType = SERVICE_TYPE

        }, NsdManager.PROTOCOL_DNS_SD, registrationListener)
    }

    fun discover() = manager.discoverServices(SERVICE_TYPE, NsdManager.PROTOCOL_DNS_SD,
            discoveryListener?.let { return } ?: initializeDiscoveryListener().also {
                discoveryListener = it
            })

    fun connect(info: NsdServiceInfo, callback: ConnectionCallback? = null) {
        connectionCallback = callback
        manager.resolveService(info, resolveListener)
    }

    fun release() {
        listener = null
        stopDiscovering()
        stopService()
    }

    fun stopDiscovering() {
        discoveryListener?.let {
            manager.stopServiceDiscovery(it)
            discoveryListener = null
        }
    }

    fun stopService() {
        registrationListener?.let {
            manager.unregisterService(it)
            registrationListener = null
        }
    }

    //region NsdManager's listeners
    private fun initializeRegistrationListener() = object : NsdManager.RegistrationListener {
        override fun onUnregistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
            Log.d(TAG, "onUnregistrationFailed: $serviceInfo, errorCode = $errorCode")
            manager.unregisterService(this).also { registrationListener = null }
        }

        override fun onServiceUnregistered(serviceInfo: NsdServiceInfo) {
            Log.d(TAG, "onServiceUnregistered: $serviceInfo")
            registrationListener = null
            current = null
            handler.post { listener?.onServiceUnregistered() }
        }

        override fun onRegistrationFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
            Log.d(TAG, "onRegistrationFailed: $serviceInfo, errorCode = $errorCode")
            manager.unregisterService(this).also { registrationListener = null }
        }

        override fun onServiceRegistered(serviceInfo: NsdServiceInfo) {
            Log.d(TAG, "onServiceRegistered: $serviceInfo")
            current = serviceInfo
            handler.post { listener?.onServiceRegistered() }
        }
    }

    private fun initializeDiscoveryListener() = object : NsdManager.DiscoveryListener {
        override fun onServiceFound(serviceInfo: NsdServiceInfo) {
            if (serviceInfo.serviceName != current?.serviceName) {
                Log.d(TAG, "onServiceFound: $serviceInfo")
                handler.post { listener?.onServiceFound(serviceInfo) }
            }
        }

        override fun onStopDiscoveryFailed(serviceType: String?, errorCode: Int) {
            Log.d(TAG, "onStopDiscoveryFailed: $serviceType, errorCode = $errorCode")
            manager.stopServiceDiscovery(this).also { discoveryListener = null }
        }

        override fun onStartDiscoveryFailed(serviceType: String?, errorCode: Int) {
            Log.d(TAG, "onStartDiscoveryFailed: $serviceType, errorCode = $errorCode")
            manager.stopServiceDiscovery(this).also { discoveryListener = null }
        }

        override fun onDiscoveryStarted(serviceType: String?) {
            Log.d(TAG, "onDiscoveryStarted: $serviceType")
        }

        override fun onDiscoveryStopped(serviceType: String?) {
            Log.d(TAG, "onDiscoveryStopped: $serviceType")
        }

        override fun onServiceLost(serviceInfo: NsdServiceInfo) {
            Log.d(TAG, "onServiceLost: $serviceInfo")
            handler.post { listener?.onServiceLost(serviceInfo) }
        }
    }

    private fun initializeResolveListener() = object : NsdManager.ResolveListener {
        override fun onResolveFailed(serviceInfo: NsdServiceInfo?, errorCode: Int) {
            Log.d(TAG, "onResolveFailed: $serviceInfo, errorCode = $errorCode")
            connectionCallback?.invoke(null).also { connectionCallback = null }
        }

        override fun onServiceResolved(serviceInfo: NsdServiceInfo?) {
            Log.d(TAG, "onServiceResolved: $serviceInfo")
            connectionCallback?.invoke(serviceInfo).also { connectionCallback = null }
        }
    }
    //endregion
}