import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeAll
import java.util.*

internal class ConfigurationTest {

    @Test
    fun `test parsePropertiesFile 1`() {
        val propertiesText = """
            # Server socket will listen to this port.
            serverPort = 1234
            # The number of bytes in each array. Checksum will be counted for each array of given size.
            byteCount = 12
            # The number of clients that can be handled simultaneously. Default value is 1000.
            maximumClients = 1000
        """.trimIndent()
        assertEquals(
            Configuration(serverPort = 1234, byteCount = 12, maximumClients = 1000),
            parsePropertiesFile(propertiesText.byteInputStream())
        )
    }

    @Test
    fun `test parsePropertiesFile 2`() {
        val propertiesText = """
            serverPo#rt = 0
            # The number of ####'s
            byteCount = 0
            maximumClients =
        """.trimIndent()
        assertEquals(
            Configuration(serverPort = DEFAULT_SERVER_PORT, byteCount = 0, maximumClients = DEFAULT_MAXIMUM_CLIENTS),
            parsePropertiesFile(propertiesText.byteInputStream())
        )
    }

    @Test
    fun `test parsePropertiesFile 3`() {
        val propertiesText = """
            serverPort = 543213213124124213
            byteCount = bcda
            max
        """.trimIndent()
        assertEquals(
            Configuration(serverPort = DEFAULT_SERVER_PORT, byteCount = DEFAULT_BYTE_COUNT, maximumClients = DEFAULT_MAXIMUM_CLIENTS),
            parsePropertiesFile(propertiesText.byteInputStream())
        )
    }

    @Test
    fun `test loadIntProperty`() {
        val props = Properties().apply {
            setProperty("a", "1")
            setProperty("b", "abc")
            setProperty("c", "9999999999999")
        }
        assertEquals(1, loadIntProperty(props, "a", 1234))
        assertEquals(1234, loadIntProperty(props, "b", 1234))
        assertEquals(1234, loadIntProperty(props, "c", 1234))
        assertEquals(1234, loadIntProperty(props, "d", 1234))
    }

    @Test
    fun `test loadConfiguration`() {
        assertDoesNotThrow {
            loadConfiguration()
        }
    }

}