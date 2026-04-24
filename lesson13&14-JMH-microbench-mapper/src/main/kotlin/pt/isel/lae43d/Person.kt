package pt.isel.lae43d

import pt.isel.mappers.AlternativeName

class Person(val name: String, @AlternativeName(name = "from") val country: String) {

}
