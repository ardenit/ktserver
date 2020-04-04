
// Since UByte is still experimental, Int is used here.
fun calculateCheckSum(bytes: List<Int>): Int =
    bytes.fold(0) { a, b -> (a + b) % 256 } % 256