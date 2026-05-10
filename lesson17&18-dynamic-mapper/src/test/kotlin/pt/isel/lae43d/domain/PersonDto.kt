package pt.isel.lae43d.domain

import pt.isel.lae43d.AlternativeName

class PersonDto(
    val name: String,
    @AlternativeName("country") val from: String,
)

