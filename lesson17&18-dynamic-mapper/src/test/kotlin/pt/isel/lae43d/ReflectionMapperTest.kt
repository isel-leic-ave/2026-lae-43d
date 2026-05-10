package pt.isel.lae43d

import pt.isel.lae43d.domain.Person
import pt.isel.lae43d.domain.PersonDto
import pt.isel.lae43d.specific.PersonDto2Person
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals


class  ReflectionMapperTest: MapperTest() {
    override fun getMapper(
        src: KClass<PersonDto>,
        dst: KClass<Person>
    ): Mapper<PersonDto, Person> {
        return PersonDto2Person()
    }
}
