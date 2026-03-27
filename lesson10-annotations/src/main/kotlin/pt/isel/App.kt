package pt.isel

import kotlin.reflect.KCallable
import kotlin.reflect.KProperty
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.memberFunctions
import kotlin.reflect.full.memberProperties

// @TagColor("blue")
@TagTeam(team = "Portugal")
class Student(
    // @property:TagColor("White") val name: String,
    @TagColor("White") val name: String,
    val age: Int,
) {
    @TagTeam(team = "portugal")
    fun print() = println("I am a Student")
}

fun main() {
    Student::class
        .annotations
        .forEach { annotation -> println(annotation) }
    println(Student::class.hasAnnotation<TagTeam>())
    println(Student::class.hasAnnotation<TagColor>())
    Student::class
        .memberProperties
        .forEach { prop: KProperty<*> ->
            val tag = prop.findAnnotation<TagColor>()
            println(prop.name + " -- color: " + tag?.color)
        }
    Student::class
        .memberFunctions
        .forEach { func: KCallable<*> ->
            val tag = func.findAnnotation<TagColor>()
            println(func.name + " -- color: " + tag?.color)
        }
}
