package pt.isel

import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.net.URI
import java.time.LocalDate

fun checkMembers(obj: Any) {
    val objType: Class<*> = obj::class.java // <=> .kotlin.java
    println("### " + objType.name)
    objType
        .declaredMethods
        .forEach { println("Method " + it.name) }

    objType
        .declaredFields
        .forEach { println("Field " + it.name) }
}

fun checkAndCallMethods(obj: Any) {
    val objType: Class<*> = obj::class.java // <=> .kotlin.java
    println("### " + objType.name)
    objType
        .declaredMethods
        .filter { func ->
            func.parameters.size == 0 &&
                func.modifiers and Modifier.PUBLIC != 0
        }.forEach { func: Method ->
            println("Func ${func.name}(): ${func.returnType} ======> ${func.invoke(obj)}")
        }
}

fun main() {
    // checkMembers(URI("https://github.com"))
    // checkMembers(LocalDate.now())
    checkAndCallMethods(URI("https://github.com"))
    checkAndCallMethods(LocalDate.now())
}
