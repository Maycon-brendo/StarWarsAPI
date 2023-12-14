package br.com.infnet.Assessment;

import br.com.infnet.Assessment.controllers.PersonController;
import br.com.infnet.Assessment.exception.ResourceNotFoundException;
import br.com.infnet.Assessment.model.Person;
import br.com.infnet.Assessment.model.PersonRequest;
import br.com.infnet.Assessment.model.StarWarsCharacter;
import br.com.infnet.Assessment.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PersonControllerTest {

    private PersonController personController;
    private PersonService personService;

    @BeforeEach
    void setUp() {
        personService = new PersonService();
        personController = new PersonController(personService);
    }

    @Test
    @DisplayName("Deve criar uma pessoa com sucesso")
    void testCreatePerson() {
        PersonRequest personRequest = new PersonRequest();
        personRequest.setName("luke skywalker");
        personRequest.setAge(30);
        personRequest.setHobbies(Arrays.asList("jedi", "aturar rey"));

        ResponseEntity<String> response = personController.createPerson(personRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Pessoa criada com sucesso", response.getBody());
    }

    @Test
    @DisplayName("Deve obter uma lista de pessoas")
    void testGetPeople() {
        ResponseEntity<List<Person>> response = personController.getPeople(null, null);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Deve deletar uma pessoa com sucesso")
    void testDeletePerson() {
        PersonRequest personRequest = new PersonRequest();
        personRequest.setName("leia skywalker");
        personRequest.setAge(25);
        personRequest.setHobbies(Arrays.asList("ser rebelde", "liderar"));

        personController.createPerson(personRequest);

        ResponseEntity<String> deleteResponse = personController.deletePerson(1L);

        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());
        assertEquals("Pessoa deletada com sucesso", deleteResponse.getBody());
    }

    @Test
    @DisplayName("Deve atualizar uma pessoa com sucesso")
    void testUpdatePerson() {
        PersonRequest personRequest = new PersonRequest();
        personRequest.setName("han");
        personRequest.setAge(40);
        personRequest.setHobbies(Arrays.asList("contrabando", "pirata espacial"));

        personController.createPerson(personRequest);

        PersonRequest updatedPerson = new PersonRequest();
        updatedPerson.setName("han att");
        updatedPerson.setAge(41);
        updatedPerson.setHobbies(Arrays.asList("contrabando", "pirata espacial", "cafageste espacial"));

        ResponseEntity<String> updateResponse = personController.updatePerson(1L, updatedPerson);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertEquals("Pessoa atualizada com sucesso", updateResponse.getBody());
    }

    @Test
    @DisplayName("Deve obter um personagem de Star Wars com sucesso")
    void testGetStarWarsCharacter() {
        ResponseEntity<StarWarsCharacter> response = personController.getStarWarsCharacter(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    @DisplayName("Deve retornar 404 ao obter um personagem de Star Wars inexistente")
    void testGetStarWarsCharacterNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> personController.getStarWarsCharacter(999L));
    }

    @Test
    @DisplayName("Deve obter todos os personagens com sucesso")
    void testGetAllPeople() {
        ResponseEntity<List<Person>> response = personController.getAllPeople();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
