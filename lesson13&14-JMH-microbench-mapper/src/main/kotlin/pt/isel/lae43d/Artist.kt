package pt.isel.lae43d

import pt.isel.mappers.AlternativeName

class Artist(
    val kind: String,
    val name: String,
    @AlternativeName("state") val country: Country,
    //@AlternativeName("songs") map: List<Track>
) {

}

class Country(val name: String, val idiom: String) {

}
