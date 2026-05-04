package pt.isel.lae43d

interface Mapper<T, R> {
    fun mapFrom(src: T): R
    fun mapFromList(src: List<T>): List<R> = src.map { this.mapFrom(it) }
}