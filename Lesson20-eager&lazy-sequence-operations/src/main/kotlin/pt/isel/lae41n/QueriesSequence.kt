package pt.isel.lae41n


/**
 * Returns a sequence containing the results of applying the given
 * transform function to each element in the original collection.
 */
fun <T, R> Sequence<T>.lazyMapGenerator(transform: (T) -> R): Sequence<R> {
    return sequence {
        for (item in this@lazyMapGenerator)
            this.yield(transform(item))
        }
}

/**
 * Returns a sequence containing only elements matching the given [predicate].
 * Simplistic implementation that does not work with nullable items.
 */
fun <T> Sequence<T>.lazyFilterGenerator(predicate: (T) -> Boolean): Sequence<T> {
    return sequence {
        for (item in this@lazyFilterGenerator) {
            if (predicate(item)) {
                yield(item)
            }
        }
    }


}
//
///**
// * Returns a sequence containing only distinct elements from the given collection.
// */
//fun <T> Sequence<T>.lazyDistinct(): Sequence<T> {
//    TODO()
//}
//
///**
// * Returns a sequence containing all elements of original sequence and
// * then all elements of the given elements sequence.
// */
//fun <T> Sequence<T>.lazyConcat(other: Sequence<T>): Sequence<T> {
//    TODO()
//}
//
///**
// * Merges series of adjacent elements.
// */
//fun <T : Any?> Sequence<T>.lazyCollapse(): Sequence<T> {
//    TODO()
//}
//
///**
// * Returns a sequence of values built from the elements of `this` sequence and the [other] sequence with the same index
// * using the provided [transform] function applied to each pair of elements.
// * The resulting sequence ends as soon as the shortest input sequence ends.
// */
//fun <T, R, V> Sequence<T>.lazyZip(
//    other: Sequence<R>,
//    transform: (a: T, b: R) -> V,
//): Sequence<V> {
//    TODO()
//}
