package net.wuerl.wormhole.client

import io.ktor.client.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.websocket.*
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.runBlocking
import net.wuerl.wormhole.client.http.webSocket
import net.wuerl.wormhole.client.protocol.*
import ru.nsk.kstatemachine.*

val url = Url("ws://relay.magic-wormhole.io:4000/v1")
val url3 = Url("wss://mailbox.mw.leastauthority.com/v1")


fun main() {

    val machine = getMachine()
    val wrapper = MachineWrapper(machine)

    val client = HttpClient {
        install(WebSockets)
    }

    runBlocking {
        client.webSocket(url3) {
            while (true) {
                val message = incoming.receive() as? Frame.Text
                if (message != null) {
                    wrapper.processMessage(message, outgoing)
                }
            }
        }
    }
    client.close()
    println("Connection closed. Goodbye!")
}
