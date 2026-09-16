package github.romulomotadev.msclients.service;

import github.romulomotadev.msclients.dto.ClientDto;
import github.romulomotadev.msclients.entities.Client;
import github.romulomotadev.msclients.exception.exceptions.DuplicateResourceException;
import github.romulomotadev.msclients.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msclients.factory.ClientFactory;
import github.romulomotadev.msclients.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClientServiceTest {

    @InjectMocks
    private ClientService service;

    @Mock
    private ClientRepository repository;


    // ======= DADOS ========

    private Long existingId;
    private Long nonExistingId;
    private String existingDocument;
    private String nonExistingDocument;

    private Client client;
    private ClientDto clientDto;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        existingDocument = "12345678901";
        nonExistingDocument = "98765432109";

        client = ClientFactory.createClient();
        clientDto = new ClientDto(client);
    }


    // ======= SAVE ========

    // SALVA QUANDO EMAIL NAO EXISTENTE
    @Test
    @DisplayName("save deve retornar cliente quando email não existir")
    void saveShouldReturnClientWhenEmailNotExists() {

        // PREPARA
        when(repository.existsByEmail(client.getEmail())).thenReturn(false);
        when(repository.save(any(Client.class))).thenReturn(client);

        // EXECUTA
        clientDto = service.save(clientDto);

        // VERIFICA
        assertNotNull(clientDto);
        assertEquals(client.getId(), clientDto.getId());
        assertEquals(client.getName(), clientDto.getName());
        assertEquals(client.getPerson().getType(), clientDto.getPerson().getType());
        assertEquals(client.getAddresses().getFirst().getAddress(), clientDto.getAddresses().getFirst().getAddress());

        verify(repository).save(any(Client.class));
    }


    //EMAIL JA EXISTENTE
    @Test
    @DisplayName("save deve lançar duplicate resource exception quando email existir")
    void saveShouldThrowDuplicateResourceExceptionWhenEmailExists() {

        // PREPARA
        when(repository.existsByEmail(client.getEmail())).thenReturn(true);

        // EXECUTA + VERIFICA
        assertThrows(DuplicateResourceException.class,
                () -> service.save(clientDto));
    }


    // ======= GET ========

    // BUSCA POR ID EXISTENTE
    @Test
    @DisplayName("findById deve retornar Client DTO quando ID existir")
    void findByIdShouldReturnClientDTOWhenIdExists() {

        // PREPARA
        when(repository.findById(existingId)).thenReturn(Optional.of(client));

        // EXECUTA
        clientDto = service.findById(existingId);

        // VERIFICA
        assertNotNull(clientDto);
        assertEquals(client.getId(), clientDto.getId());
        assertEquals(client.getName(), clientDto.getName());
        assertEquals(client.getPerson().getType(), clientDto.getPerson().getType());
        assertEquals(client.getAddresses().getFirst().getAddress(), clientDto.getAddresses().getFirst().getAddress());

        verify(repository).findById(existingId);
        verifyNoMoreInteractions(repository);
    }


    // BUSCA POR ID NÃO EXISTENTE
    @Test
    @DisplayName("findById deve lançar Resource Not Found Exception quando ID não existir")
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        // PREPARA
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // EXECUTA + VERIFICA
        assertThrows(ResourceNotFoundException.class, () ->
            service.findById(nonExistingId));
    }


    // BUSCA POR DOCUMENTO EXISTENTE
    @Test
    @DisplayName("findByPersonDocument deve retornar Client DTO quando document existir")
    void findByDocumentShouldReturnClientDTOWhenDocumentExists(){

        // PREPARA
        when(repository.existsByPersonDocument(existingDocument)).thenReturn(true);
        when(repository.findByPersonDocument(existingDocument)).thenReturn(client);

        // EXECUTA
        clientDto = service.findByPersonDocument(existingDocument);

        // VERIFICA
        assertNotNull(clientDto);
        assertEquals(client.getId(), clientDto.getId());
        assertEquals(client.getName(), clientDto.getName());
        assertEquals(client.getPerson().getType(), clientDto.getPerson().getType());
        assertEquals(client.getPerson().getDocument(), clientDto.getPerson().getDocument());
        assertEquals(client.getAddresses().getFirst().getAddress(), clientDto.getAddresses().getFirst().getAddress());

        verify(repository).findByPersonDocument(existingDocument);
        verifyNoMoreInteractions(repository);
    }


    // BUSCA POR DOCUMENTO NÃO EXISTENTE
    @Test
    @DisplayName("findByPersonDocument deve lançar Resource Not Found Exception quando document não existir")
    void findByPersonDocumentShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        // PREPARA
        when(repository.existsByPersonDocument(nonExistingDocument)).thenReturn(false);

        // EXECUTA + VERIFICA
        assertThrows(ResourceNotFoundException.class, () ->
            service.findByPersonDocument(nonExistingDocument));
    }


    // BUSCA TODOS CLIENTE
    @Test
    @DisplayName("findAll deve retornar lista de Client DTO")
    void findAllShouldReturnListOfClientDTO() {

        // PREPARA
        when(repository.findAll()).thenReturn(List.of(client));

        // EXECUTA
        List<ClientDto> result = service.findAll();

        // VERIFICA
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }


    // BUSCA TODOS SEM HAVER CLIENTES
    @Test
    @DisplayName("findAll deve retornar lista vazia quando não houver dados")
    void findAllShouldReturnEmptyListWhenNoData() {

        // PREPARA
        when(repository.findAll()).thenReturn(List.of());

        // EXECUTA
        List<ClientDto> result = service.findAll();

        // VERIFICA
        assertTrue(result.isEmpty());

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }


    // ======= DELETE ========

    // DELETE POR ID
    @Test
    @DisplayName("delete deve remover quando ID existir")
    void deleteShouldRemoveWhenIdExists() {

        // PREPARA
        when(repository.findById(existingId)).thenReturn(Optional.of(client));

        // EXECUTA
        service.delete(existingId);

        // VERIFICA
        verify(repository).findById(existingId);
        verify(repository).deleteById(existingId);
        verifyNoMoreInteractions(repository);
    }


    // DELETE QUANDO ID NAO EXISTENTE
    @Test
    @DisplayName("delete deve lançar Resource Not Found Execption quando ID não existir")
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        // PREPARA
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () ->
            service.delete(nonExistingId));
    }


    // ======= UPDATE ========

    // ATUALIZA QUANDO ID EXISTENTE
    @Test
    @DisplayName("update deve atualizar e retornar Client DTO quando ID existir")
    void updateShouldUpdateAndReturnClientDTOWhenIdExists() {

        // PREPARAR
        when(repository.findById(existingId)).thenReturn(Optional.of(client));
        when(repository.save(any(Client.class))).thenReturn(client);

        // EXECUTA
        ClientDto result = service.update(clientDto, existingId);

        // VERIFICA
        assertNotNull(result);
        assertEquals(existingId, result.getId());

        verify(repository).findById(existingId);
        verify(repository).save(any(Client.class));
        verifyNoMoreInteractions(repository);
    }


    // ATUALIZA ID NÃO EXISTENTE
    @Test
    @DisplayName("update deve lançar Resource Not Found Exception quando ID não existir")
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        // PREPARA
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // EXECUTA + VERIFICA
        assertThrows(ResourceNotFoundException.class, () ->
            service.update(clientDto, nonExistingId));
    }
}
