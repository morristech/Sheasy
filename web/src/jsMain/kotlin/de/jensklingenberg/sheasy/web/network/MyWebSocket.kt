package de.jensklingenberg.sheasy.web.network

import org.w3c.dom.MessageEvent
import org.w3c.dom.WebSocket

import org.w3c.dom.events.Event
import org.w3c.dom.events.EventListener

class MyWebSocket(url: String) : Websocket {


    var listener: WebSocketListener? = null
    val webSocket = WebSocket(url)

    override  fun addListener(listener:WebSocketListener){
        this.listener=listener

        webSocket.onmessage = { event: Event ->
            listener?.onMessage((event as MessageEvent))
        }

        webSocket.onerror = { event: Event ->
            listener?.onError((event))
        }



        webSocket.addEventListener("dd", EventListener { })
    }


    override fun open() {
        webSocket.onopen = { tt: Event ->
            console.log(tt)
            webSocket.send("Hugner")
        }
    }



    override fun close() {
        listener=null
        webSocket.close()

    }


    override fun send(message: String) {
        webSocket.send(message)

    }

    interface WebSocketListener {
        fun onMessage(messageEvent: MessageEvent)
        fun onError(messageEvent: Event)
    }


}