package pt.isel.lae43d

import pt.isel.lae43d.domain.Person
import pt.isel.lae43d.domain.PersonDto
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals


abstract class  MapperTest {
    @Test
    fun `Test mapping PersonDto to Person`() {
        val mapper: Mapper<PersonDto, Person> = getMapper(PersonDto::class, Person::class)
        val dto = PersonDto("Maria", "Portugal")
        val person = mapper.mapFrom(dto)
        assertEquals("Maria", person.name)
        assertEquals("Portugal", person.country)
        //assertEquals(2001, person.bornYear)
    }

    protected abstract fun getMapper(
        klass: KClass<PersonDto>,
        klass2: KClass<Person>
    ): Mapper<PersonDto, Person>;

}
