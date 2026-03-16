fun foo(o: Any) {}

fun bar(i: Int) {}

fun inc(nr: Int?) : Int {
    requireNotNull(nr)
    return nr + 1
}

fun xpto() {
    l: List<Int> = listOf(1, 2, 3)
}

fun main() {
    var x: Int? = null
    var y: Int = 10

    println(y)

    val n: Int = 7
    val o: Any = n // Boxing
    var s = o.toString()
    foo(7) // Boxing
    inc(11)
}
