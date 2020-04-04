import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.util.*
import java.util.logging.FileHandler
import java.util.logging.Logger
import java.util.logging.SimpleFormatter

private const val PROPERTIES_PATH = "./servercfg.properties"

const val DEFAULT_SERVER_PORT = 54321
const val DEFAULT_BYTE_COUNT = 20
const val DEFAULT_MAXIMUM_CLIENTS = 1000

data class Configuration(
    val serverPort: Int = DEFAULT_SERVER_PORT,
    val byteCount: Int = DEFAULT_BYTE_COUNT,
    val maximumClients: Int = DEFAULT_MAXIMUM_CLIENTS
)

fun loadConfiguration(): Configuration =
    try {
        val propertiesInputStream = FileInputStream(PROPERTIES_PATH)
        val configuration = parsePropertiesFile(propertiesInputStream)
        propertiesInputStream.close()
        log.info("Configuration is successfully loaded.")
        configuration
    } catch (ex: FileNotFoundException) {
        log.warning("Configuration file not found. Default values will be used.")
        Configuration(DEFAULT_SERVER_PORT, DEFAULT_BYTE_COUNT, DEFAULT_MAXIMUM_CLIENTS)
    } catch (ex: IOException) {
        log.warning("Error while reading a configuration file. Default values will be used.")
        Configuration(DEFAULT_SERVER_PORT, DEFAULT_BYTE_COUNT, DEFAULT_MAXIMUM_CLIENTS)
    }

fun parsePropertiesFile(propertiesInputStream: InputStream): Configuration {
    val properties = Properties()
    properties.load(propertiesInputStream)
    val serverPort = loadIntProperty(properties, "serverPort", DEFAULT_SERVER_PORT)
    val byteCount = loadIntProperty(properties, "byteCount", DEFAULT_BYTE_COUNT)
    val maximumClients = loadIntProperty(properties, "maximumClients", DEFAULT_MAXIMUM_CLIENTS)
    return Configuration(serverPort, byteCount, maximumClients)
}

fun loadIntProperty(properties: Properties, key: String, defaultValue: Int): Int =
    properties.getProperty(key, null)
        ?.toIntOrNull() ?: run {
        log.warning("$key configuration is not valid. Default value will be used.")
        defaultValue
    }