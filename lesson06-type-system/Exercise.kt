/**
 * Question: Identify in the code bellow the occurrences of checkcast, boxing and unboxing operations.
 */
fun cold(a: List<Double?>, b: Any): Double {
    val p = 1234
    var q: Double? = a.first()
    if (q == null) { 
        q = p.toDouble() 
    }
    return q + b as Double
}

fun main() {
    var d: Double = 2.0
    cold(listOf(2.1, 2.2), d)
}


