import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

private const val LOG_FILE_PATH = "./server.log"

val log: Logger = Logger.getLogger("log")

fun configureLoggingToFile() {
    val fileHandler = FileHandler(LOG_FILE_PATH)
    fileHandler.formatter = SimpleFormatter()
    log.addHandler(fileHandler)
}