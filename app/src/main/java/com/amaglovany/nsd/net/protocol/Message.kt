package com.amaglovany.nsd.net.protocol

import com.amaglovany.nsd.extension.className
import java.io.InputStream
import java.io.OutputStream

const val CURRENT_VERSION = 1

/**
 * @param raw type of the Message
 * @param length size of body
 */
sealed class Message(val version: Int, val raw: Int, val length: Int, val body: ByteArray) {
    open protected fun body(): String = body.toString()
    override fun toString() = "${this.className}[ver=$version, raw=$raw, " +
            "len=$length, body=${body()}]"

    fun writeTo(stream: OutputStream) {
        stream.write(version)
        stream.write(raw)
        stream.write(length)
        stream.write(body)
        stream.flush()
    }
}

/**
 * client name
 * @param body byte array that represents utf-8 encoded name String
 */
class Greeting(length: Int, body: ByteArray) : Message(CURRENT_VERSION, 0, length, body) {
    private constructor(nameArray: ByteArray) : this(nameArray.size, nameArray)
    constructor(name: String) : this(name.toByteArray())

    override fun body() = name
    val name: String get() = String(body)
}

/**
 * type of the data to be transferred
 * @param body 1 byte that specifies Type of data
 * @see Type
 */
class DataType(body: ByteArray) : Message(CURRENT_VERSION, 1, 1, body) {
    constructor(@Type type: Int) : this(byteArrayOf(type.toByte()))

    override fun body() = type.toString()
    val type: Int get() = body[0].toInt()
}

/**
 * the name of the data to be transferred
 * @param body ByteArray that represents utf-8 encoded name String
 */
class DataName(length: Int, body: ByteArray) : Message(CURRENT_VERSION, 2, length, body) {
    private constructor(nameArray: ByteArray) : this(nameArray.size, nameArray)
    constructor(name: String) : this(name.toByteArray())

    override fun body() = name
    val name: String get() = String(body)
}

/**
 * preview of the data to be transferred
 * @param body ByteArray that represents image preview of the data.
 * The body is optional and can be zero length array which means preview is absent
 */
class Preview(length: Int, body: ByteArray) : Message(CURRENT_VERSION, 3, length, body)

/** request of data transferring begin. Used to determine that all required info is sent */
class TransferRequest : Message(CURRENT_VERSION, 4, 0, byteArrayOf())

/**
 * confirmation from the server for data transfer
 * @param body 1 byte that specifies confirmation type
 * @see ConfirmationType
 */
class Confirmation(body: ByteArray) : Message(CURRENT_VERSION, 5, 1, body) {
    constructor(accept: Boolean) : this(byteArrayOf((if (accept) {
        ConfirmationType.ACCEPTED
    } else ConfirmationType.DECLINED).toByte()))

    override fun body() = isAccepted.toString()
    val isAccepted: Boolean get() = body[0].toInt() == ConfirmationType.ACCEPTED
}

/**
 * the data to be transferred
 * @param body ByteArray of the data
 */
class Data(length: Int, body: ByteArray) : Message(CURRENT_VERSION, 6, length, body)

/** indicates successful end of the transferring process */
class Completion : Message(CURRENT_VERSION, 7, 0, byteArrayOf())

object Parser {

    fun parse(stream: InputStream): Message? {
        val version = stream.read()
        val raw = stream.read()
        val length = stream.read()

        if (version != CURRENT_VERSION) return null

        return when (raw) {
            0 -> Greeting(length, stream.readArray(length))
            1 -> DataType(stream.readArray(1))
            2 -> DataName(length, stream.readArray(length))
            3 -> Preview(length, stream.readArray(length))
            4 -> TransferRequest()
            5 -> Confirmation(stream.readArray(1))
            6 -> Data(length, stream.readArray(length))
            7 -> Completion()

            else -> null
        }
    }

    private fun InputStream.readArray(length: Int): ByteArray {
        val array = ByteArray(length)
        read(array)
        return array
    }
}