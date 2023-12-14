package br.com.infnet.Assessment.controllers;

import br.com.infnet.Assessment.exception.ResourceNotFoundException;
import br.com.infnet.Assessment.model.Person;
import br.com.infnet.Assessment.model.PersonRequest;
import br.com.infnet.Assessment.model.StarWarsCharacter;
import br.com.infnet.Assessment.service.PersonService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/people")
public class PersonController {

    private static  final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @PostMapping
    public ResponseEntity<String> createPerson(@RequestBody PersonRequest personRequest) {
        try {
            personService.createPerson(personRequest);
            LOGGER.info("Pessoa criada com sucesso. Status Code: {}", HttpStatus.OK.value());
            return ResponseEntity.ok("Pessoa criada com sucesso");



        } catch (Exception e) {
            LOGGER.error("Erro ao criar  pessoa STATUS CODE: {}", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao criar pessoa");
        }

    }

    @GetMapping
    public ResponseEntity<List<Person>> getPeople(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "age", required = false) Integer age) {
        try {
            List<Person> people = personService.getPeople(name, age);
            LOGGER.info("Pessoa retornad  com sucesso: {}", HttpStatus.OK.value());
            return ResponseEntity.ok(people);
        } catch (Exception e) {
            LOGGER.error("Erro ao retornar pessoa STATUS CODE: {}", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePerson(@PathVariable Long id) {
        try {
            personService.deletePerson(id);
            LOGGER.info("Pessoa deletada com sucesso: {}", HttpStatus.OK.value());
            return ResponseEntity.ok("Pessoa deletada com sucesso");
        } catch (Exception e) {
            LOGGER.error("Erro ao deletar pessoa STATUS CODE: {}", HttpStatus.INTERNAL_SERVER_ERROR.value());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao deletar pessoa");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updatePerson(@PathVariable Long id, @RequestBody PersonRequest personRequest) {
        try {
            personService.updatePerson(id, personRequest);
            LOGGER.info("atualizado com sucesso STATUS CODE: {}", HttpStatus.OK.value());
            return ResponseEntity.ok("Pessoa atualizada com sucesso");
        } catch (ResourceNotFoundException e) {
            LOGGER.error("Pessoa não encontrada para atualização. STATUS CODE: {}", HttpStatus.NOT_FOUND.value(), e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa não encontrada para atualização");
        } catch (Exception e) {
            LOGGER.error("Erro ao atualizar pessoa STATUS CODE: {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao atualizar pessoa");
        }
    }

    @GetMapping("/star-wars/{id}")
    public ResponseEntity<StarWarsCharacter> getStarWarsCharacter(@PathVariable Long id) {
        try {
            LOGGER.info("requisição ok. STATUS CODE: {}", HttpStatus.OK.value());
            StarWarsCharacter character = personService.getStarWarsCharacter(id);
            return ResponseEntity.ok(character);
        } catch (ResourceNotFoundException e) {
            LOGGER.error("Personagem de Star Wars não encontrado. STATUS CODE: {}", HttpStatus.NOT_FOUND.value(), e);
            throw e;
        } catch (Exception e) {
            LOGGER.error("Erro ao obter personagem de Star Wars. STATUS CODE: {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/todos")
    public ResponseEntity<List<Person>> getAllPeople() {
        try {
            List<Person> people = personService.getAllPeople();
            LOGGER.info("todos retornado. STATUS CODE: {}", HttpStatus.OK.value());
            return ResponseEntity.ok(people);
        } catch (Exception e) {
            LOGGER.error("erro em retornar todos STATUS CODE: {}", HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

