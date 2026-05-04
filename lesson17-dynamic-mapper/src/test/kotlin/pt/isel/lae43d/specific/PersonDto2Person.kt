package pt.isel.lae43d.specific

import pt.isel.lae43d.Mapper
import pt.isel.lae43d.domain.Person
import pt.isel.lae43d.domain.PersonDto

class PersonDto2Person : Mapper<PersonDto, Person> {
    override fun mapFrom(personDto: PersonDto): Person {
        return Person(personDto.name, personDto.from)
    }
}