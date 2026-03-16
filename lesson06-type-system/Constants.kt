const val BITS_OF_10KB = 8 * 10 * 1024

// Error: const 'val' initializer must be a constant value. 
// const val BITS_OF_5KB = calcBitsOfKb(5)

val BITS_OF_5KB = calcBitsOfKb(5)

fun calcBitsOfKb(words: Int) : Int {
    return words * 8 * 1024
}