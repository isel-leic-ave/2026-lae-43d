package pt.isel.lae43d

interface A {
    fun f1();
    fun f2(a: List<A>)  {
        a.forEach { f1() }
    }
}

open abstract class  Base(): A {
    override fun f2(a: List<A>) {
        a.forEach { f1() }
    }
}

class X(): Base(), A {
    override fun f1() {
        TODO("Not yet implemented")
    }

}

class Y(): A {
    override fun f1() {
        TODO("Not yet implemented")
    }
}


fun doSomethingWithA(a: A) {
    a.f1()
}

