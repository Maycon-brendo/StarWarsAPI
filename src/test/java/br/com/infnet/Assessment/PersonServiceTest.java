package br.com.infnet.Assessment;
import br.com.infnet.Assessment.exception.ResourceNotFoundException;
import br.com.infnet.Assessment.model.Person;
import br.com.infnet.Assessment.model.PersonRequest;
import br.com.infnet.Assessment.model.StarWarsCharacter;
import br.com.infnet.Assessment.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
public class PersonServiceTest {

    private static  final Logger LOGGER = LoggerFactory.getLogger(PersonServiceTest.class);

    private PersonService personService;

    @BeforeEach
    public void setUp() {

        personService = new PersonService(new RestTemplate());
    }

    @Test
    @DisplayName("Deve criar um perspnagem na lista")
    public void testaCreatePerson() {

        PersonRequest personRequest = new PersonRequest();
        personRequest.setName("luke skywalker");
        personRequest.setAge(25);
        personRequest.setHobbies(Arrays.asList("treinamento jedi", "aturar a rey"));

        assertEquals("luke skywalker", personRequest.getName());
        assertEquals(25, personRequest.getAge());
    }

    @Test
    @DisplayName("Deve retornar usando o filtro nome e idade")
    public void testaGetPeopleWithMatchingNameAndAge() {

        List<Person> people = Arrays.asList(
                new Person(1L, "luke", 30),
                new Person(2L, "leia", 25),
                new Person(3L, "han", 35)
        );
        PersonService personService = new PersonService(new RestTemplate(), people);



        List<Person> result = personService.getPeople("luke", 30);


        assertEquals(1, result.size());
        assertEquals("luke", result.get(0).getName());
        assertEquals(30, result.get(0).getAge());
    }

    @Test
    @DisplayName("Deve retornar usando o filtro nome")
    public void testaGetPeopleWithMatchingNameOnly() {

        List<Person> people = Arrays.asList(
                new Person(1L, "luke", 30),
                new Person(2L, "leia", 25),
                new Person(3L, "han", 35)
        );
        PersonService personService = new PersonService(new RestTemplate(), people);


        List<Person> result = personService.getPeople("leia", null);

        assertEquals(1, result.size());
        assertEquals("leia", result.get(0).getName());
        assertEquals(25, result.get(0).getAge());
    }

    @Test
    @DisplayName("Deve retornar usando o filtro de idade")
    public void testaGetPeopleWithMatchingAgeOnly() {

        List<Person> people = Arrays.asList(
                new Person(1L, "luke", 30),
                new Person(2L, "leia", 25),
                new Person(3L, "han", 35)
        );
        PersonService personService = new PersonService(new RestTemplate(), people);


        List<Person> result = personService.getPeople(null, 35);


        assertEquals(1, result.size());
        assertEquals("han", result.get(0).getName());
        assertEquals(35, result.get(0).getAge());
    }

    @Test
    @DisplayName("Deve retorna a lista inteira de personagens, pois está sem filtro")
    public void testaGetPeopleWithNoFilters() {

        List<Person> people = Arrays.asList(
                new Person(1L, "luke", 30),
                new Person(2L, "leia", 25),
                new Person(3L, "han", 35)
        );
        PersonService personService = new PersonService(new RestTemplate(), people);


        List<Person> result = personService.getPeople(null, null);


        assertEquals(3, result.size());
    }

    @Test
    @DisplayName("Deve retorna a lista inteira de personagens")
    public void testaGetAllPeople() {

        List<Person> people = Arrays.asList(
                new Person(1L, "luke", 30),
                new Person(2L, "leia", 25),
                new Person(3L, "han", 35)
        );


        personService = new PersonService(new RestTemplate(), people);


        List<Person> result = personService.getAllPeople();


        assertEquals(3, result.size());
        assertEquals(people, result);
    }

    @Test
    @DisplayName("Deve deletar um dos personagens pelo id")
    public void testaDeletePerson() {

        List<Person> people = Arrays.asList(
                new Person(1L, "luke", 30),
                new Person(2L, "leia", 25),
                new Person(3L, "han", 35)
        );


        personService = new PersonService(new RestTemplate(), people);


        personService.deletePerson(2L);


        List<Person> result = personService.getAllPeople();
        assertEquals(2, result.size());
        assertTrue(result.stream().noneMatch(person -> person.getId().equals(2L)));
        LOGGER.info("personagem deletado");
    }

    @Test
    @DisplayName("Deve atualizar um dos personagens")
    public void testaUpdatePerson() {
        // Arrange
        List<Person> people = Arrays.asList(
                new Person(1L, "luke", 30),
                new Person(2L, "leia", 25),
                new Person(3L, "han", 35)
        );


        personService = new PersonService(new RestTemplate(), people);


        LOGGER.info("Dados da pessoa a ser atualizada");
        Long personId = 2L;
        PersonRequest updatedPersonData = new PersonRequest();
        updatedPersonData.setName("leia atualizada");
        updatedPersonData.setAge(28);
        updatedPersonData.setHobbies(Arrays.asList("novo hobby"));


        personService.updatePerson(personId, updatedPersonData);


        List<Person> result = personService.getAllPeople();
        assertEquals(3, result.size());


        LOGGER.info("Verifica se a pessoa foi atualizada corretamente");
        Person updatedPerson = result.stream().filter(p -> p.getId().equals(personId)).findFirst().orElse(null);
        assertNotNull(updatedPerson);
        assertEquals("leia atualizada", updatedPerson.getName());
        assertEquals(28, updatedPerson.getAge());
        assertEquals(Arrays.asList("novo hobby"), updatedPerson.getHobbies());
    }

    @Test
    @DisplayName("Deve retornar um personagem da consulta da api de SW externa")
    public void testaGetStarWarsCharacter() {

        PersonService personService = new PersonService();
        StarWarsCharacter starWarsCharacter = personService.getStarWarsCharacter(1L);

        assertEquals("Luke Skywalker", starWarsCharacter.getName());
    }

    @Test
    @DisplayName("Deve retornar uma exceção para um personagem inexistente")
    public void testaPersonagemInexistente() {
        PersonService personService = new PersonService();

        assertThrows(ResourceNotFoundException.class, () -> {
            personService.getStarWarsCharacter(300L);
        });
    }
}
