
fun main() {
    configureLoggingToFile()
    val configuration = loadConfiguration()
    val server = Server(configuration)
    server.start()
}