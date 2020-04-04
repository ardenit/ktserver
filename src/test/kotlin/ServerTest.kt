import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.net.Socket
import kotlin.concurrent.thread

internal class ServerTest {

    @Test
    fun `test single client`() {
        val server = Server(Configuration(serverPort = 54321, byteCount = 1, maximumClients = 1))
        val serverThread = thread {
            server.start()
        }
        val clientThread = thread {
            testOneClient()
        }
        clientThread.join()
        server.stop()
        serverThread.join()
    }

    @Test
    fun `test multiple clients`() {
        val server = Server(Configuration(serverPort = 54321, byteCount = 1, maximumClients = 5))
        val serverThread = thread {
            server.start()
        }
        val clientThreads = ArrayList<Thread>()
        repeat(5) {
            clientThreads += thread {
                testOneClient()
            }
        }
        clientThreads.forEach { it.join() }
        server.stop()
        serverThread.join()
    }

    @Test
    fun `test clients number overflow`() {
        val server = Server(Configuration(serverPort = 54321, byteCount = 1, maximumClients = 2))
        val serverThread = thread {
            server.start()
        }
        val clientThreads = ArrayList<Thread>()
        repeat(8) {
            clientThreads += thread {
                testOneClient()
            }
        }
        clientThreads.forEach { it.join() }
        server.stop()
        serverThread.join()
    }

    private fun testOneClient() {
        val client = Socket("localhost", 54321)
        client.getOutputStream().run {
            write(1)
            flush()
            Thread.sleep(10L)
            write(2)
            flush()
        }
        client.getInputStream().run {
            assertEquals(1, read())
            assertEquals(2, read())
        }
        client.close()
    }

}