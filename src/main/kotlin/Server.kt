import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors

class Server(private val configuration: Configuration) {

    private val serverSocket = ServerSocket(configuration.serverPort)
    private val clientSockets = ConcurrentHashMap<Socket, Unit>()
    private val executor = Executors.newFixedThreadPool(configuration.maximumClients)

    fun start() {
        try {
            while (true) {
                log.info("Waiting for a new client....")
                val clientSocket = serverSocket.accept()
                clientSockets[clientSocket] = Unit
                log.info("New client has connected: ${clientSocket.inetAddress}")
                executor.submit {
                    handleClient(clientSocket)
                }
            }
        } catch (ex: SocketException) {
            log.info("Server socket is closed.")
        }
    }

    fun stop() {
        log.info("Shutting down the server....")
        serverSocket.close()
        clientSockets.keys.forEach { it.close() }
        executor.shutdown()
    }

    fun handleClient(clientSocket: Socket) {
        try {
            log.info("Started handling client ${clientSocket.inetAddress}")
            val inputStream = clientSocket.getInputStream()
            val outputStream = clientSocket.getOutputStream()
            val bytes = ArrayList<Int>()
            while (!clientSocket.isClosed) {
                val receivedByte: Int = inputStream.read()
                if (receivedByte != -1) {
                    bytes += receivedByte
                    if (bytes.size == configuration.byteCount) {
                        log.info("Enough bytes has been received from the client ${clientSocket.inetAddress}.")
                        log.info("Calculating checksum for client ${clientSocket.inetAddress}....")
                        outputStream.write(calculateCheckSum(bytes))
                        outputStream.flush()
                        bytes.clear()
                        log.info("Checksum for client ${clientSocket.inetAddress} has been calculated and sent.")
                    }
                } else {
                    log.info("Closing the socket with client ${clientSocket.inetAddress}....")
                    clientSocket.close()
                    clientSockets.remove(clientSocket)
                }
            }
        } catch (ex: SocketException) {
            log.warning("Error while handling client ${clientSocket.inetAddress}. Connection will be closed.")
            clientSocket.close()
            clientSockets.remove(clientSocket)
        }
        log.info("Socket with client ${clientSocket.inetAddress} has been closed.")
    }

}