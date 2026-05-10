package pt.isel;

import pt.isel.lae43d.Mapper;
import pt.isel.lae43d.domain.Person;
import pt.isel.lae43d.domain.PersonDto;


public class PersonDto2PersonBaseline implements Mapper<PersonDto, Person> {
    @Override
    public Person mapFrom(PersonDto src) {
        return new Person(src.getName(), src.getFrom());
    }
}
