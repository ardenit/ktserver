import java.io.File
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

private const val LOG_FOLDER_PATH = "./logs"
private const val LOG_FILE_NAME = "server%u.log"

val log: Logger = Logger.getLogger("log")

fun configureLoggingToFile() {
    val logFolder = File(LOG_FOLDER_PATH)
    if (!logFolder.exists()) logFolder.mkdir()
    val fileHandler = FileHandler("$LOG_FOLDER_PATH/$LOG_FILE_NAME")
    fileHandler.formatter = SimpleFormatter()
    log.addHandler(fileHandler)
}