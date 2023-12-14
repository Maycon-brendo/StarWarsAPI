package br.com.infnet.Assessment;

import br.com.infnet.Assessment.model.Person;
import br.com.infnet.Assessment.model.PersonRequest;
import br.com.infnet.Assessment.model.StarWarsCharacter;
import br.com.infnet.Assessment.exception.ResourceNotFoundException;
import br.com.infnet.Assessment.service.PersonService;
import lombok.extern.slf4j.Slf4j;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;


@Slf4j
class AssessmentApplicationTests {

	private PersonService personService;

	@BeforeEach
	void setUp() {
		personService = new PersonService();
	}

	@Test
	@DisplayName("Deve criar um perspnagem na lista")
	void testaCreatePerson() {
		PersonRequest personRequest = new PersonRequest();
		personRequest.setName("luke skywalker");
		personRequest.setAge(25);
		personRequest.setHobbies(Arrays.asList("treinamento jedi", "aturar a rey"));

		personService.createPerson(personRequest);

		List<Person> allPeople = personService.getAllPeople();
		assertEquals(1, allPeople.size());

		Person createdPerson = allPeople.get(0);
		assertEquals("luke skywalker", createdPerson.getName());
		assertEquals(25, createdPerson.getAge());
		assertEquals(Arrays.asList("treinamento jedi", "aturar a rey"), createdPerson.getHobbies());
	}

	@Test
	@DisplayName("Deve retornar todos os personagens")
	void testaGetAllPeople() {
		List<Person> allPeople = personService.getAllPeople();
		assertNotNull(allPeople);
		assertTrue(allPeople.isEmpty());
	}

	@Test
	@DisplayName("Deve excluir um personagem")
	void testaDeletePerson() {

		PersonRequest personRequest = new PersonRequest();
		personRequest.setName("leia");
		personRequest.setAge(20);
		personRequest.setHobbies(Arrays.asList("rebelde", "sensitiva"));

		personService.createPerson(personRequest);

		List<Person> allPeopleBeforeDeletion = personService.getAllPeople();
		assertEquals(1, allPeopleBeforeDeletion.size());

		Person createdPerson = allPeopleBeforeDeletion.get(0);
		long personId = createdPerson.getId();


		personService.deletePerson(personId);

		List<Person> allPeopleAfterDeletion = personService.getAllPeople();
		assertTrue(allPeopleAfterDeletion.isEmpty());
	}

	@Test
	@DisplayName("Deve atualizar o perspnagem")
	void testaUpdatePerson() {

		PersonRequest personRequest = new PersonRequest();
		personRequest.setName("luke");
		personRequest.setAge(40);
		personRequest.setHobbies(Arrays.asList("jedi", "treinar"));

		personService.createPerson(personRequest);

		List<Person> allPeopleBeforeUpdate = personService.getAllPeople();
		assertEquals(1, allPeopleBeforeUpdate.size());

		Person createdPerson = allPeopleBeforeUpdate.get(0);
		long personId = createdPerson.getId();


		PersonRequest updatedPersonRequest = new PersonRequest();
		updatedPersonRequest.setName("luke 123");
		updatedPersonRequest.setAge(41);
		updatedPersonRequest.setHobbies(Arrays.asList("jedi2", "jedi3"));

		personService.updatePerson(personId, updatedPersonRequest);

		List<Person> allPeopleAfterUpdate = personService.getAllPeople();
		assertEquals(1, allPeopleAfterUpdate.size());

		Person updatedPerson = allPeopleAfterUpdate.get(0);
		assertEquals("luke 123", updatedPerson.getName());
		assertEquals(41, updatedPerson.getAge());
		assertEquals(Arrays.asList("jedi2", "jedi3"), updatedPerson.getHobbies());
	}

	@Test
	@DisplayName("Deve retornar um personagem da api externa por id")
	void testaGetStarWarsCharacter() {
		StarWarsCharacter starWarsCharacter = personService.getStarWarsCharacter(1L);
		assertNotNull(starWarsCharacter);
		assertEquals("Luke Skywalker", starWarsCharacter.getName());
	}

	@Test
	@DisplayName("Retorna exceção")
	void testaGetStarWarsCharacterNotFound() {
		assertThrows(ResourceNotFoundException.class, () -> {
			personService.getStarWarsCharacter(999L);
		});
	}
}
