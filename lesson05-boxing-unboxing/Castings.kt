class A

class B : A()

class B1 : A()

class C: B()

fun foo(b: B) {
    val o: Any = a
    val b = a as B // bytecode checkcast
}


fun main() {
    println("Test an object B")
    foo(B())
    readLine()
    println("Test an object A")
    foo(A())
}