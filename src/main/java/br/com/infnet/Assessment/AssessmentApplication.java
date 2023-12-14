package br.com.infnet.Assessment;
import br.com.infnet.Assessment.exception.ResourceNotFoundException;
import br.com.infnet.Assessment.model.Person;
import br.com.infnet.Assessment.model.PersonRequest;
import br.com.infnet.Assessment.model.StarWarsCharacter;
import br.com.infnet.Assessment.service.PersonService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class AssessmentApplication implements CommandLineRunner {

	private final PersonService personService;

	public AssessmentApplication(PersonService personService) {
		this.personService = personService;
	}

	public static void main(String[] args) {
		SpringApplication.run(AssessmentApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Scanner scanner = new Scanner(System.in);
        try{
            while (true) {
                System.out.println("Bem vindo ao universo Star Wars");
                System.out.println("Escolha uma opção:");
                System.out.println("1. Registrar um personagem");
                System.out.println("2. Listar todas os personagens");
                System.out.println("3. Atualizar um personagem");
                System.out.println("4. Excluir um personagem");
                System.out.println("5. Consultar informações dos personagens na API de Star Wars");
                System.out.println("0. Sair");

                int opcao = scanner.nextInt();
                scanner.nextLine();

                switch (opcao) {
                    case 1:
                        criarPessoa(scanner);
                        break;
                    case 2:
                        listarPessoas();
                        break;
                    case 3:
                        atualizarPessoa(scanner);
                        break;
                    case 4:
                        excluirPessoa(scanner);
                        break;
                    case 5:
                        consultarApiStarWars(scanner);
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        System.exit(0);
                    default:
                        System.out.println("Opção inválida. Tente novamente.");
                }
            }
        } catch (InputMismatchException e) {
            System.out.println("Opção inválida. Tente novamente.");
            scanner.nextLine();
        }
	}


	private void criarPessoa(Scanner scanner) {
        try {
            System.out.println("Digite o nome da pessoa:");
            String nome = scanner.nextLine();

            System.out.println("Digite a idade da pessoa:");
            int idade = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Digite os hobbies da pessoa (separados por vírgula):");
            String hobbiesInput = scanner.nextLine();
            List<String> hobbies = List.of(hobbiesInput.split(","));

            PersonRequest personRequest = new PersonRequest();
            personRequest.setName(nome);
            personRequest.setAge(idade);
            personRequest.setHobbies(hobbies);

            personService.createPerson(personRequest);

            System.out.println("Personagem criado com sucesso!");
        }catch (InputMismatchException e) {
            System.out.println("Valor inválido. Tente novamente.");
            scanner.nextLine();
        }
	}

	private void listarPessoas() {
		List<Person> pessoas = personService.getAllPeople();

		if (pessoas.isEmpty()) {
			System.out.println("Nenhuma pessoa cadastrada.");
		} else {
			System.out.println("Lista de personagens:");
			for (Person pessoa : pessoas) {
				System.out.println("ID: " + pessoa.getId() + ", Nome: " + pessoa.getName() + ", Idade: " + pessoa.getAge() + ", Hobbies: " + pessoa.getHobbies());
			}
		}
	}

	private void atualizarPessoa(Scanner scanner) {
        try {
            if(personService.getAllPeople().isEmpty()){
                System.out.println("Lista vazia. Não há personagem para Atuializar.");
                return;
            }
            System.out.println("Digite o ID da pessoa que deseja atualizar:");
            long id = scanner.nextLong();
            scanner.nextLine();

            System.out.println("Digite o novo nome da pessoa:");
            String nome = scanner.nextLine();

            System.out.println("Digite a nova idade da pessoa:");
            int idade = scanner.nextInt();
            scanner.nextLine();

            System.out.println("Digite os novos hobbies da pessoa (separados por vírgula):");
            String hobbiesInput = scanner.nextLine();
            List<String> hobbies = List.of(hobbiesInput.split(","));

            PersonRequest personRequest = new PersonRequest();
            personRequest.setName(nome);
            personRequest.setAge(idade);
            personRequest.setHobbies(hobbies);

            personService.updatePerson(id, personRequest);

            System.out.println("Pessoa atualizada com sucesso!");
        }catch (InputMismatchException e) {
            System.out.println("Valor inválido. Tente novamente.");
            scanner.nextLine();
        }
	}

	private void excluirPessoa(Scanner scanner) {
        try {
            if(personService.getAllPeople().isEmpty()){
                System.out.println("Lista vazia. Não há personagem para Excluir.");
                return;
            }
            System.out.println("Digite o ID da pessoa que deseja excluir:");
            long id = scanner.nextLong();
            scanner.nextLine();

            personService.deletePerson(id);

            System.out.println("Pessoa excluída com sucesso!");
        }catch (InputMismatchException e) {
            System.out.println("ID inválido. Tente novamente.");
            scanner.nextLine();
        }
	}

    private void consultarApiStarWars(Scanner scanner) {
        try {
            System.out.println("Digite o ID do personagem de Star Wars que deseja consultar:");
            long id = scanner.nextLong();
            scanner.nextLine();

            try {
                StarWarsCharacter character = personService.getStarWarsCharacter(id);

                System.out.println("Detalhes do personagem de Star Wars:");
                System.out.println("Nome: " + character.getName());
                System.out.println("Altura: " + character.getHeight());
                System.out.println("Cor do cabelo: " + character.getHair_color());
                System.out.println("Cor da pele: " + character.getSkin_color());
                System.out.println("Cor dos olhos: " + character.getEye_color());
                System.out.println("Ano de nascimento: " + character.getBirth_year());
                System.out.println("Gênero: " + character.getGender());

            } catch (ResourceNotFoundException e) {
                System.out.println(e.getMessage());
            }

        } catch (InputMismatchException e) {
            System.out.println("ID inválido. Tente novamente.");
            scanner.nextLine();
        }
    }

}
