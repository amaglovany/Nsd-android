package com.amaglovany.nsd.net

import com.amaglovany.nsd.model.Wrapper

interface MessageReceiver {
    fun onMessageReceived(message: Wrapper)
}