package pt.isel

import pt.isel.lae41n.lazyCollapse
import pt.isel.lae41n.lazyConcat
import pt.isel.lae41n.lazyZip
import kotlin.test.Test
import kotlin.test.assertContentEquals

class TestQueries {
    @Test
    fun `lazyConcat with two sequences`() {
        val sequence1 = sequenceOf("a", "b")
        val sequence2 = sequenceOf("z", "x", "y")
        val res = sequence1.lazyConcat(sequence2)

        assertContentEquals(listOf("a", "b", "z", "x", "y"), res.toList())
    }

    @Test
    fun `lazyCollapse removes consecutive duplicates`() {
        val sequence = sequenceOf(null, null, 1, 2, 2, 7, 2, 1, 7, null, 1, 9, 9)
        val res = sequence.lazyCollapse()

        assertContentEquals(listOf(null, 1, 2, 7, 2, 1, 7, null, 1, 9), res.toList())
    }

    @Test
    fun `lazyZip stops when first sequence is longer`() {
        val sequence1 = sequenceOf(1, 2, 3, 4)
        val sequence2 = sequenceOf("a", "b")

        val result = sequence1.lazyZip(sequence2) { a, b -> "$a:$b" }.toList()

        // Zipping stops at the shortest sequence
        assertContentEquals(listOf("1:a", "2:b"), result)
    }

    @Test
    fun `lazyZip stops when second sequence is longer`() {
        val sequence1 = sequenceOf(10, 20)
        val sequence2 = sequenceOf("x", "y", "z")

        val result = sequence1.lazyZip(sequence2) { a, b -> "$a-$b" }.toList()

        // Zipping stops at the shortest sequence
        assertContentEquals(listOf("10-x", "20-y"), result)
    }
}
