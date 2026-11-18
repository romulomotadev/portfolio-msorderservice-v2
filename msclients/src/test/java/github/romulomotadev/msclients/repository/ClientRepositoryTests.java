package github.romulomotadev.msclients.repository;


import github.romulomotadev.msclients.entities.Client;
import github.romulomotadev.msclients.factory.ClientFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;


@DataJpaTest
public class ClientRepositoryTests {

    //================== DEPENDÊNCIAS ==================

    @Autowired
    private ClientRepository clientRepository;

    //================== ATRIBUTOS ==================

    private Client client;

    //================== INICIALIZAÇÃO ==================

    @BeforeEach
    void setUp() {

        //Factory
        client = ClientFactory.createClient();
        client.setId(null);
        client.getPerson().setId(null);
    }

    //================== GET ==================

    //DOCUMENTO EXISTENTE
    @Test
    @DisplayName("Busca por documento deve retornar cliente quando documento existir ")
    void findByPersonDocumentShouldReturnClientWhenDocumentExists(){

        // PREPARA
        Client save = clientRepository.save(client);
        String existingDocument = save.getPerson().getDocument();

        //EXECUTA
        Client result = clientRepository.findByPersonDocument(existingDocument);

        //VALIDAÇÃO
        Assertions.assertNotNull(result);
        Assertions.assertEquals(save.getId(), result.getId());
        Assertions.assertEquals(save.getPerson().getId(), result.getPerson().getId());
        Assertions.assertEquals(save.getPerson().getDocument(), result.getPerson().getDocument());
    }

    //DOCUMENTO INEXISTENTE
    @Test
    @DisplayName("Deve retornar null quando documento não existir")
    void findByPersonDocumentShouldReturnNullWhenDocumentDoesNotExist() {

        // PREPARA
        clientRepository.save(client);
        String nonExistingDocument = "999.999.999-99";

        //EXECUTA
        Client result = clientRepository.findByPersonDocument(nonExistingDocument);

        // ASSERT
        Assertions.assertNull(result);
    }

    //EMAIL EXISTENTE
    @Test
    @DisplayName("Existe email deve retorna verdadeiro quando email existir")
    void existsByEmailShouldReturnTrueWhenEmailExists() {

        // PREPARA
        Client save = clientRepository.save(client);
        String existingEmail = save.getEmail();

        //EXECUTA
        boolean result = clientRepository.existsByEmail(existingEmail);

        //VALIDAÇÃO
        Assertions.assertTrue(result);
    }

    //EMAIL INEXISTENTE
    @Test
    @DisplayName("Existe email deve retorna falso quando email não existir")
    void existsByEmailShouldReturnFalseWhenEmailDoesNotExist() {

        // PREPARA
        clientRepository.save(client);
        String nonExistingEmail = "falseEmail@email.com";

        //EXECUTA
        boolean result = clientRepository.existsByEmail(nonExistingEmail);

        //VALIDAÇÃO
        Assertions.assertFalse(result);
    }

    //DOCUMENTO EXISTENTE
    @Test
    @DisplayName("Existe documento deve retorna verdadeiro quando documento existir")
    void existsByPersonDocumentShouldReturnTrueWhenDocumentExists() {

        // PREPARA
        Client save = clientRepository.save(client);
        String existingDocument = save.getPerson().getDocument();

        //EXECUTA
        boolean result = clientRepository.existsByPersonDocument(existingDocument);

        //VALIDAÇÃO
        Assertions.assertTrue(result);
    }

    //DOCUMENTO INEXISTENTE
    @Test
    @DisplayName("Existe documento deve retorna falso quando documento não existir")
    void existsByPersonDocumentShouldReturnFalseWhenDocumentDoesNotExist() {

        // PREPARA
        clientRepository.save(client);
        String nonExistingDocument = "999.999.999-99";
        //EXECUTA
        boolean result = clientRepository.existsByPersonDocument(nonExistingDocument);

        //VALIDAÇÃO
        Assertions.assertFalse(result);
    }

}
