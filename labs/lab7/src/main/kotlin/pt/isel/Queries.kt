package pt.isel

/**
 * Returns a list containing the results of applying the given
 * transform function to each element in the original collection.
 */
fun <T, R> Iterable<T>.eagerMap(transform: (T) -> R): Iterable<R> {
    val destination = mutableListOf<R>()
    for (item in this) {
        destination.add(transform(item))
    }
    return destination
}

// Eager: T1T2T3 -> MapEager -> Iterable -> Iterator -> R1R2R3
// Lazy: T1T2T3 -> MapLazy -> Iterable


/**
 * Returns a list containing only elements matching the given predicate.
 */
fun <T> Iterable<T>.eagerFilter(predicate: (T) -> Boolean): Iterable<T> {
    val destination = mutableListOf<T>()
    for(element in this) {
        if(predicate(element)) {
            destination.add(element)
        }
    }
    return destination
}

/**
 * Returns a list containing only distinct elements from the given collection.
 */
fun <T> Iterable<T>.eagerDistinct(): Iterable<T> {
    val destination = mutableSetOf<T>()
    for (item in this) {
        destination.add(item)
    }
    return destination
}

/**
 * Returns a sequence containing the results of applying the given
 * transform function to each element in the original collection.
 */
fun <T, R> Sequence<T>.lazyMap(transform: (T) -> R): Sequence<R> {
    val upstreamSeq = this
    return object: Sequence<R> {
        override fun iterator(): Iterator<R> {
            var upstreamIter: Iterator<T> = upstreamSeq.iterator()
            return object: Iterator<R> {
                override fun next(): R {
                    return transform(upstreamIter.next())
                }

                override fun hasNext(): Boolean {
                    return upstreamIter.hasNext()
                }
            }
        }
    }
}
/**
 * Returns a sequence containing only elements matching the given [predicate].
 * Simplistic implementation that does not work with nullable items.
 */
fun <T> Sequence<T>.lazyFilter(predicate: (T) -> Boolean): Sequence<T> {
    return object : Sequence<T> {
        override fun iterator(): Iterator<T> {
            val upstream = this@lazyFilter.iterator()
            return object : Iterator<T> {
                private var nextItem: T? = null
                private var nextReady = false

                override fun hasNext(): Boolean {
                    while (upstream.hasNext()) {
                        val item = upstream.next()
                        if (predicate(item)) {
                            nextItem = item
                            nextReady = true
                            return true
                        }
                    }
                    return false
                }

                override fun next(): T {
                    if (!nextReady && !hasNext()) {
                        throw NoSuchElementException()
                    }
                    nextReady = false
                    return nextItem as T
                }
            }
        }
    }
}

/**
 * Returns a sequence containing only distinct elements from the given collection.
 */
fun <T> Sequence<T>.lazyDistinct(): Sequence<T> {
    TODO()
}

/**
 * Returns a sequence containing all elements of original sequence and
 * then all elements of the given elements sequence.
 */
fun <T> Sequence<T>.lazyConcat(other: Sequence<T>): Sequence<T> {
    TODO()
}

/**
 * Merges series of adjacent elements.
 */
fun <T : Any?> Sequence<T>.lazyCollapse(): Sequence<T> {
    TODO()
}

/**
 * Returns a sequence of values built from the elements of `this` sequence and the [other] sequence with the same index
 * using the provided [transform] function applied to each pair of elements.
 * The resulting sequence ends as soon as the shortest input sequence ends.
 */
fun <T, R, V> Sequence<T>.lazyZip(
    other: Sequence<R>,
    transform: (a: T, b: R) -> V,
): Sequence<V> {
    TODO()
}
