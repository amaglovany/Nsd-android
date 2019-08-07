package com.amaglovany.nsd.net

import android.net.nsd.NsdServiceInfo
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.amaglovany.nsd.model.Wrapper
import com.amaglovany.nsd.net.protocol.Message
import com.amaglovany.nsd.net.protocol.Parser
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.IOException
import java.net.InetAddress
import java.net.ServerSocket
import java.net.Socket

typealias CreationCallback = (port: Int) -> Unit
typealias ConnectionCallback = (info: NsdServiceInfo?) -> Unit

class Connection {

    companion object {
        private const val TAG = "CONN"
    }

    val isServerRunning: Boolean get() = server?.thread?.isInterrupted == false
    val isAnyoneConnected: Boolean get() = clients.isNotEmpty()

    private val handler = Handler(Looper.getMainLooper())

    private val clients = mutableSetOf<Client>()
    private var server: Server? = null
    private var callback: CreationCallback? = null

    @Volatile private var localPort = -1

    fun create(callback: CreationCallback? = null): Connection {
        destroy()
        this.callback = callback
        server = Server()
        return this
    }

    fun destroy() {
        server?.release()
    }

    @Synchronized
    fun connect(address: InetAddress, port: Int, receiver: MessageReceiver? = null) {
        val client = clients.find { it.hashCode() == address.hashCode() }
                ?: Client(address, port).also { clients.add(it) }
        client.receiver = receiver
    }

    fun send(address: InetAddress, message: Message) {
        clients.find { it.hashCode() == address.hashCode() }?.send(message)
    }

    fun disconnect(address: InetAddress) {
        val client = clients.find { it.hashCode() == address.hashCode() } ?: return
        client.release()
    }

    fun disconnectAll() {
        while (clients.isNotEmpty()) {
            clients.last().release()
        }
    }

    fun release() {
        destroy()
        disconnectAll()
    }

    //region Internal
    private fun hasClientFor(address: InetAddress, port: Int) = clients.find {
        it.hashCode() == address.hashCode() && it.port == port
    } != null

    private fun connect(socket: Socket) {
        clients.add(Client(socket))
    }

    private inner class Server {
        lateinit var serverSocket: ServerSocket
        val thread = Thread(ServerThread()).also { it.start() }

        fun release() {
            thread.interrupt()
            try {
                serverSocket.close()
            } catch (e: IOException) {
                Log.e(TAG, "release: error when closing server socket", e)

            } finally {
                server = null
            }
        }

        private inner class ServerThread : Runnable {
            override fun run() {
                try {
                    serverSocket = ServerSocket(0)
                    localPort = serverSocket.localPort
                    callback?.invoke(localPort)

                    while (!Thread.currentThread().isInterrupted) {
                        Log.d(TAG, "[ServerThread] run: awaiting connection")
                        val socket = serverSocket.accept()
                        Log.d(TAG, "[ServerThread] run: connected")

                        if (socket != null && !hasClientFor(socket.inetAddress, socket.port)) {
                            Log.d(TAG, "[ServerThread] run: connecting to our server")
                            connect(socket)
                        }
                    }

                } catch (e: IOException) {
                    if (!Thread.currentThread().isInterrupted) {
                        Log.d(TAG, "[ServerThread] run: error creating ServerSocket ${this@Server}", e)
                    }

                } finally {
                    release()
                }
            }
        }
    }

    private inner class Client(private val address: InetAddress, internal val port: Int) {

        constructor(socket: Socket) : this(socket.inetAddress, socket.port) {
            this.socket = socket
        }

        internal var receiver: MessageReceiver? = null
        private var socket: Socket? = null
        val receiverThread = Thread(ReceivingThread()).also { it.start() }

        fun send(message: Message) = Thread(SendingThread(message)).start()

        fun release() {
            try {
                receiverThread.interrupt()
                receiver = null
                socket?.close()

            } catch (e: Exception) {
                Log.d(TAG, "[Client] error while release", e)

            } finally {
                socket = null
                clients.remove(this)
            }
        }

        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as Client
            return address == other.address && port == other.port
        }

        override fun hashCode() = address.hashCode()

        private inner class ReceivingThread : Runnable {
            override fun run() {
                val s = socket ?: Socket(address, port).also { socket = it }
                val inputStream = BufferedInputStream(s.getInputStream())
                try {
                    while (!Thread.currentThread().isInterrupted && !s.isClosed) {
                        val message = Parser.parse(inputStream) ?: break
                        handler.post { receiver?.onMessageReceived(Wrapper(message.toString(), false)) }
                        Log.d(TAG, "[Client RT] receive: $message")
                    }

                } catch (e: Exception) {
                    Log.d(TAG, "[Client RT] run: something went wrong ${this@Client}", e)

                } finally {
                    if (!Thread.currentThread().isInterrupted) release()
                }
            }
        }

        private inner class SendingThread(private val message: Message) : Runnable {
            override fun run() {
                val s = socket ?: return
                val outputStream = BufferedOutputStream(s.getOutputStream() ?: return)
                Log.d(TAG, "run: outputStream = $outputStream")
                try {
                    message.writeTo(outputStream)
                    handler.post { receiver?.onMessageReceived(Wrapper(message.toString(), true)) }
                    Log.d(TAG, "[Client ST] send: $message")

                } catch (e: Exception) {
                    Log.d(TAG, "[Client ST] run: something went wrong ${this@Client}", e)
                }
            }
        }
    }
    //endregion
}