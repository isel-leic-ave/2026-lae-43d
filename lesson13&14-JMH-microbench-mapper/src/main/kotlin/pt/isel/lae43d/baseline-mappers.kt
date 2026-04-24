package pt.isel.lae43d

/**
 * Mapper from PersonDto to Person
 */
fun PersonDto.toPerson(): Person =
    Person(
        name = this.name,
        country = this.from,
    )


/**
 * Mapper from ArtistDto to Artist
 */
fun ArtistDto.toArtist(): Artist =
    Artist(
        kind,
        name,
        Country(state.name, state.idiom)
        //songs.map { Track(it.title, it.year) },
    )