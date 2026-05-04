package pt.isel.lae43d

import pt.isel.lae43d.domain.Person
import pt.isel.lae43d.domain.PersonDto
import kotlin.test.Test
import kotlin.test.assertEquals


class DynamicMapperTest {
    @Test
    fun `Test mapping PersonDto to Person`() {
        val mapper: Mapper<PersonDto, Person> = loadDynamicMapper(PersonDto::class, Person::class)
        val dto = PersonDto("Maria", "Portugal")
        val person = mapper.mapFrom(dto)
        assertEquals("Maria", person.name)
        assertEquals("Portugal", person.country)
        //assertEquals(2001, person.bornYear)
    }
}
