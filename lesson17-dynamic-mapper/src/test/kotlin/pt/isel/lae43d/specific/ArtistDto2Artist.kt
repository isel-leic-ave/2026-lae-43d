package pt.isel.lae43d.specific

import pt.isel.lae43d.Mapper
import pt.isel.lae43d.domain.Artist
import pt.isel.lae43d.domain.ArtistDto
import pt.isel.lae43d.domain.Country
import pt.isel.lae43d.domain.Person
import pt.isel.lae43d.domain.PersonDto
import pt.isel.lae43d.domain.StateDto
import pt.isel.lae43d.loadDynamicMapper

class ArtistDto2Artist : Mapper<ArtistDto, Artist> {
    override fun mapFrom(artistDto: ArtistDto): Artist {
        val kind = artistDto.kind
        val name = artistDto.name
        val stateDto2CountryMapper = loadDynamicMapper(StateDto::class.java, Country::class.java)
        val country = stateDto2CountryMapper.mapFrom(artistDto.state)
        return Artist(kind, name, country)
    }
}