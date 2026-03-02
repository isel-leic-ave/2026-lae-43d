//package pt.isel

class Rectangle(private val height: Int, var width: Int)
{
    val area
         get() = height * width
    //val area = height * width

    override fun toString() : String{
        return "$width, $height"
    }
}

//fun printWidth(r: Rectangle) {
//    println(r.width)
//}


fun main() {
    var r = Rectangle(10, 20)
    println(r.area)
    r.width = 30
    println(r.area)

    var r1 = r
    println(r1)
    r = Rectangle(10, 20)
    println(r)
}