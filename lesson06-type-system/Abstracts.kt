interface I {
    fun foo()
}

abstract class A {
    abstract fun bar()
}

fun main() {
    // val i = I() // interface 'interface I : Any' does not have constructors.
    // val a = A() // cannot create an instance of an abstract class.
    
    val i = object : I {
        override fun foo() {
            println("Ola from ISEL")
        }
    }
}