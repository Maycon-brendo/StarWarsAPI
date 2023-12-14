package br.com.infnet.Assessment.service;
import br.com.infnet.Assessment.exception.ResourceNotFoundException;
import br.com.infnet.Assessment.model.Person;
import br.com.infnet.Assessment.model.PersonRequest;
import br.com.infnet.Assessment.model.StarWarsCharacter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PersonService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final RestTemplate restTemplate;
    private final List<Person> people;
    private long nextPersonId = 1;

    public PersonService() {
        this.restTemplate = new RestTemplate();
        this.people = new ArrayList<>();
    }




    public PersonService(RestTemplate restTemplate, List<Person> people) {
        this.restTemplate = restTemplate;
        this.people = new ArrayList<>(people);
    }


    public PersonService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.people = new ArrayList<>();
    }


    public void createPerson(PersonRequest personRequest) {
        try {
            Person person = new Person();
            person.setId(nextPersonId++);
            person.setName(personRequest.getName());
            person.setAge(personRequest.getAge());
            person.setHobbies(personRequest.getHobbies());

            people.add(person);
            LOGGER.info("Criada com sucesso. ID: {}, status code{}", person.getId(), HttpStatus.OK.value());
        } catch (Exception e) {
            LOGGER.error("Erro ao criar pessoa. Detalhes: {}", e.getMessage(), e);
        }
    }

    public List<Person> getAllPeople() {
        LOGGER.info("Obtendo a lista completa de pessoas STATUS CODE: {}", HttpStatus.OK.value());
        return new ArrayList<>(people);
    }

    public List<Person> getPeople(String name, Integer age) {
        List<Person> filteredPeople = new ArrayList<>();

        try {
            for (Person person : people) {
                boolean nameMatches = name == null || person.getName().contains(name);
                boolean ageMatches = age == null || Objects.equals(person.getAge(), age);

                if (nameMatches && ageMatches) {
                    LOGGER.info("Pessoa fintrada com sucesso, STATUS CODE: {}", HttpStatus.OK.value());
                    filteredPeople.add(person);
                }
            }
        } catch (Exception e) {
            LOGGER.error("Erro ao filtrar pessoas. Detalhes: {}", e.getMessage(), e);
        }

        return filteredPeople;
    }

    public void deletePerson(Long id) {
        try {
            if (people.removeIf(person -> person.getId().equals(id))) {
                LOGGER.info("Pessoa removida com sucesso. ID: {}, STATUS CODE: {}", id, HttpStatus.OK.value());
            } else {
                LOGGER.warn("Tentativa de excluir pessoa com ID inexistente. ID: {}, STATUS CODE: {}", id, HttpStatus.NOT_FOUND.value());
                throw new ResourceNotFoundException("Pessoa não encontrada para exclusão. ID: " + id);
            }
        } catch (ResourceNotFoundException e) {
            throw e;
        } catch (Exception e) {
            LOGGER.error("Erro ao excluir pessoa. ID: {}, STATUS CODE: {}", id, HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            throw new RuntimeException("Erro ao excluir pessoa", e);
        }
    }
    public void updatePerson(Long id, PersonRequest personRequest) {
        try {
            Optional<Person> optionalPerson = people.stream()
                    .filter(person -> person.getId().equals(id))
                    .findFirst();

            if (optionalPerson.isPresent()) {
                Person person = optionalPerson.get();
                person.setName(personRequest.getName());
                person.setAge(personRequest.getAge());
                person.setHobbies(personRequest.getHobbies());
                LOGGER.info("Pessoa atualizada com sucesso. ID: {}, STATUS CODE: {}", id, HttpStatus.OK.value());
            } else {
                LOGGER.warn("Tentativa de atualizar pessoa com ID inexistente. ID: {}, STATUS CODE: {}", id, HttpStatus.NOT_FOUND.value());
                throw new ResourceNotFoundException("Pessoa não encontrada para atualização. ID: " + id);
            }
        } catch (ResourceNotFoundException e) {
            throw e; // Lança a exceção para ser tratada no controlador, se necessário
        } catch (Exception e) {
            LOGGER.error("Erro ao atualizar pessoa. ID: {}, STATUS CODE: {}", id, HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            throw new RuntimeException("Erro ao atualizar pessoa", e);
        }
    }


    public StarWarsCharacter getStarWarsCharacter(Long id) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .uri(new URI("https://swapi.dev/api/people/" + id))
                    .version(HttpClient.Version.HTTP_2)
                    .build();
            HttpClient client = HttpClient.newBuilder().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            if (response.statusCode() == 404) {
                LOGGER.error("Personagem de Star Wars não encontrado para o ID: {}. Status code: {}", id, HttpStatus.NOT_FOUND.value());
                throw new ResourceNotFoundException("Personagem de Star Wars não encontrado para o ID: " + id);
            }
            ObjectMapper mapper = JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .build();


            StarWarsCharacter starWarsCharacter = mapper.readValue(response.body(), StarWarsCharacter.class);
            LOGGER.info("Personagem de Star Wars encontrado. STATUS CODE: {}", HttpStatus.OK.value());
            return starWarsCharacter;
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
