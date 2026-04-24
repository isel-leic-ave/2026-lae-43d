package pt.isel.lae43d

import pt.isel.lae43d.PersonDto
import pt.isel.lae43d.Artist
import pt.isel.lae43d.Person
import kotlin.test.Test
import kotlin.test.assertEquals
import pt.isel.mappers.*
import pt.isel.mappers.mapTo

class TestBenchMapper {
    private val ze = PersonDto("Ze Manel", "Portugal")

    private val muse =
        ArtistDto(
            "Muse",
            "Band",
            StateDto("UK", "en-UK"),
            listOf(
                SongDto("Starlight", 2006),
                SongDto("Uprising", 2009),
                SongDto("Madness", 2012),
            ).sortedBy { it.title },
        )

    @Test
    fun testReflectMapperPerson() {
        val p1 = ze.toPerson()
        val p2 = ze.mapTo(Person::class) as Person
        assertEquals(p1.name, p2.name)
        assertEquals(p1.country, p2.country)
    }

    @Test
    fun testReflectMapperPersonImproved() {
        val p1 = ze.toPerson()
        val mapperPersonDtoToPerson =
            Mapper(PersonDto::class, Person::class)
        val p2 = mapperPersonDtoToPerson.mapTo(ze) as Person
        assertEquals(p1.name, p2.name)
    }

    @Test
    fun testReflectMapperArtist() {
        val a1 = muse.toArtist()
        val a2 = muse.mapTo(Artist::class) as Artist
        assertEquals(a1.name, a2.name)
        assertEquals(a1.country.name, a2.country.name)
        assertEquals(a1.country.idiom, a2.country.idiom)
        assertEquals(a1.kind, a2.kind)
//        a1.tracks.forEachIndexed { i, t ->
//            val t2 = a2.tracks[i]
//            assertEquals(t.title, t2.title)
//            assertEquals(t.year, t2.year)
//        }
    }

//    @Test
//    fun testReflectMapperOptPerson() {
////        val mapper = MapperOpt(PersonDto::class, Person::class)
////        mapper.mapFrom(dto)
//    }
}
