package pt.isel.bytecode

import kotlin.math.sqrt

fun magnitude(x: Float, y:Float) : Float {
    val res = sqrt(x*x + y*y)
    return res
}

class Sum(initial: Int) {
    private var count = initial
    fun add(v: Int) : Int {
        count = count + v
        return count
    } 
}


fun main() {
    magnitude(2.0F,3.0F)
}
