package pt.isel.lae43d

import pt.isel.lae43d.domain.Person
import pt.isel.lae43d.domain.PersonDto
import kotlin.reflect.KClass
import kotlin.test.Test
import kotlin.test.assertEquals


class  DynamicMapperTest: MapperTest() {
    override fun getMapper(
        src: KClass<PersonDto>,
        dst: KClass<Person>
    ): Mapper<PersonDto, Person> {
        return loadDynamicMapper(src, dst)
    }

}
