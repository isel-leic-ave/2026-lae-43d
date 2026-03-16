class A {
    class B
}

class OuterClass(private val name: String) {
    inner class InnerClass {
        
        fun print() {
            // this@OuterClass.name
            println("Outer name: ${name}")
        }
    }
}