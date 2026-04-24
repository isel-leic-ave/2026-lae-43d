package pt.isel.lae43d

class ArtistDto(
    val name: String,
    val kind: String,
    val state: StateDto,
    val songs: List<SongDto>,
)
