package pt.isel

fun main() {
    //makeGarbageWithoutRootReferences()
    makeGarbageWithRootReferences()
    println("GC after the end of checkGc()")
    // Putting null on arr removes the root reference
    // to the million element objects stored in the array,
    // arr = null
    System.gc()
    printAllocatedMem()
}

var arr: Any? = null

fun makeGarbageWithoutRootReferences() {
    makeGarbage()
}

fun makeGarbageWithRootReferences() {
    arr = makeGarbage()
}

fun printAllocatedMem() {
    val runtime = Runtime.getRuntime()
    val totalMem = runtime.totalMemory()
    val freeMem = runtime.freeMemory()
    val allocatedMem = totalMem - freeMem
    println("Total MEM = ${totalMem / 1024} Kb")
    println("Free MEM = ${freeMem / 1024} Kb")
    println("Allocated MEM = ${allocatedMem / 1024} Kb")
}

const val size = 1_000_000

fun makeGarbage(): Array<Any?> {
    val arr = arrayOfNulls<Any>(size)
    for (i in 0 until size) {
        arr[i] = Any()
    }
    return arr
}
