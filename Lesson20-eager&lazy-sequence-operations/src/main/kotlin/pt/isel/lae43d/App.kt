package pt.isel.lae43d

fun main() {
    val nrs = foo(true).iterator()
    println("We have got the iterator and nothing happens")
    var current: Int = 0;
    nrs.hasNext()
    current = nrs.next()
    current = nrs.next()
    current = nrs.next()
    current = nrs.next()


//    val seq = sequenceOf(1, 2, 3)
//    val newSeq = seq.lazyMapGenerator { it * 2 }
//    val iterNewSeq = newSeq.iterator()
//    iterNewSeq.next()
//    iterNewSeq.next()
//    iterNewSeq.next()
//    iterNewSeq.next()

}

fun foo(flag: Boolean = false) : Sequence<Int> {
    return sequence<Int> {
        println("Step 1")
        yield(7)
        println("Step 2")
        yield(11)
        println("Step 3")
        yield(33)
        yieldAll(sequenceOf(2,3,4))
        if(flag) {
            return@sequence
        }
        println("Step 4")
        yield(17)
    }
}