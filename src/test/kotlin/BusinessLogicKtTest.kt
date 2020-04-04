import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class BusinessLogicKtTest {

    @Test
    fun `test checksum correctness`() {
        assertEquals(
            1,
            calculateCheckSum(listOf(0, 256, 513, 0))
        )
        assertEquals(
            1,
            calculateCheckSum(listOf(513))
        )
        assertEquals(
            8,
            calculateCheckSum(listOf(2, 518, 0, 256, 512))
        )
        assertEquals(
            255,
            calculateCheckSum(listOf(255))
        )
    }

    @Test
    fun `test checksum failures`() {
        assertDoesNotThrow {
            calculateCheckSum(listOf())
            calculateCheckSum(listOf(-1))
            calculateCheckSum(listOf(Int.MAX_VALUE, Int.MAX_VALUE))
            calculateCheckSum(listOf(0, Int.MIN_VALUE, -1, -2))
        }
    }

}