package github.romulomotadev.msclients.service;

import github.romulomotadev.msclients.dto.ClientDto;
import github.romulomotadev.msclients.entities.Client;
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


    // ======= DATA ========

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


    // ======= POST ========

    @Test
    @DisplayName("save deve salvar e retornar DTO")
    void save_ShouldSaveAndReturnDTO() {

        // ARRANGE
        when(repository.save(any(Client.class))).thenReturn(client);

        // ACT
        clientDto = service.save(clientDto);

        // ASSERT
        assertNotNull(clientDto);
        assertEquals(client.getId(), clientDto.getId());
        assertEquals(client.getName(), clientDto.getName());
        assertEquals(client.getPerson().getType(), clientDto.getPerson().getType());
        assertEquals(client.getAddresses().getFirst().getAddress(), clientDto.getAddresses().getFirst().getAddress());

        verify(repository).save(any(Client.class));
    }


    // ======= GET ========

    // FIND BY ID EXISTING
    @Test
    @DisplayName("findById deve retornar DTO quando ID existir")
    void findById_ShouldReturnDTO_WhenIdExists() {

        // ARRANGE
        when(repository.findById(existingId)).thenReturn(Optional.of(client));

        // ACT
        clientDto = service.findById(existingId);

        // ASSERT
        assertNotNull(clientDto);
        assertEquals(client.getId(), clientDto.getId());
        assertEquals(client.getName(), clientDto.getName());
        assertEquals(client.getPerson().getType(), clientDto.getPerson().getType());
        assertEquals(client.getAddresses().getFirst().getAddress(), clientDto.getAddresses().getFirst().getAddress());

        verify(repository).findById(existingId);
        verifyNoMoreInteractions(repository);
    }


    // FIND BY ID NOT EXISTING
    @Test
    @DisplayName("findById deve lançar exceção quando ID não existir")
    void findById_ShouldThrowException_WhenIdDoesNotExist() {

        // ARRANGE
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });

        verify(repository).findById(nonExistingId);
        verifyNoMoreInteractions(repository);
    }


    // FIND BY DOCUMENT EXISTING
    @Test
    @DisplayName("findByDocument deve retornar DTO quando document existir")
    void findByDocument_ShouldReturnDTO_WhenDocumentExists(){

        // ARRANGE
        when(repository.findByPersonDocument(existingDocument)).thenReturn(client);

        clientDto = service.findByPersonDocument(existingDocument);

        assertNotNull(clientDto);
        assertEquals(client.getId(), clientDto.getId());
        assertEquals(client.getName(), clientDto.getName());
        assertEquals(client.getPerson().getType(), clientDto.getPerson().getType());
        assertEquals(client.getPerson().getDocument(), clientDto.getPerson().getDocument());
        assertEquals(client.getAddresses().getFirst().getAddress(), clientDto.getAddresses().getFirst().getAddress());

        verify(repository).findByPersonDocument(existingDocument);
        verifyNoMoreInteractions(repository);
    }


    // FIND BY DOCUMENT NOT EXISTING
    @Test
    @DisplayName("findByDocument deve lançar exceção quando document não existir")
    void findByDocument_ShouldThrowException_WhenIdDoesNotExist() {

        // ARRANGE
        when(repository.findByPersonDocument(nonExistingDocument)).thenReturn(null);

        // ACT + ASSERT
        assertThrows(NullPointerException.class, () -> {
            service.findByPersonDocument(nonExistingDocument);
        });

        verify(repository).findByPersonDocument(nonExistingDocument);
        verifyNoMoreInteractions(repository);
    }


    // FIND ALL
    @Test
    @DisplayName("findAll deve retornar lista de DTO")
    void findAll_ShouldReturnListOfDTO() {

        // ARRANGE
        when(repository.findAll()).thenReturn(List.of(client));

        // ACT
        List<ClientDto> result = service.findAll();

        // ASSERT
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }


    // FIND ALL EMPTY
    @Test
    @DisplayName("findAll deve retornar lista vazia quando não houver dados")
    void findAll_ShouldReturnEmptyList_WhenNoData() {

        // ARRANGE
        when(repository.findAll()).thenReturn(List.of());

        // ACT
        List<ClientDto> result = service.findAll();

        // ASSERT
        assertTrue(result.isEmpty());

        verify(repository).findAll();
        verifyNoMoreInteractions(repository);
    }


    // ======= DELETE ========

    // DELETE ID
    @Test
    @DisplayName("delete deve remover quando ID existir")
    void delete_ShouldRemove_WhenIdExists() {

        // ARRANGE
        when(repository.findById(existingId)).thenReturn(Optional.of(client));

        // ACT
        service.delete(existingId);

        // ASSERT
        verify(repository).findById(existingId);
        verify(repository).deleteById(existingId);
        verifyNoMoreInteractions(repository);
    }


    // DELETE NOT ID
    @Test
    @DisplayName("delete deve lançar exceção quando ID não existir")
    void delete_ShouldThrowException_WhenIdDoesNotExist() {

        // ARRANGE
        when(repository.findById(nonExistingId))
                .thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });

        verify(repository).findById(nonExistingId);
        verifyNoMoreInteractions(repository);
    }


    // ======= UPDATE ========

    // UPDATE ID EXISTING
    @Test
    @DisplayName("update deve atualizar e retornar DTO quando ID existir")
    void update_ShouldUpdateAndReturnDTO_WhenIdExists() {

        // ARRANGE
        when(repository.findById(existingId)).thenReturn(Optional.of(client));

        when(repository.save(any(Client.class))).thenReturn(client);

        // ACT
        ClientDto result = service.update(clientDto, existingId);

        // ASSERT
        assertNotNull(result);
        assertEquals(existingId, result.getId());

        verify(repository).findById(existingId);
        verify(repository).save(any(Client.class));
        verifyNoMoreInteractions(repository);
    }


    // UPDATE ID NOt EXISTING
    @Test
    @DisplayName("update deve lançar exceção quando ID não existir")
    void update_ShouldThrowException_WhenIdDoesNotExist() {

        // ARRANGE
        when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        // ACT + ASSERT
        assertThrows(ResourceNotFoundException.class, () -> {
            service.update(clientDto, nonExistingId);
        });

        verify(repository).findById(nonExistingId);
        verifyNoMoreInteractions(repository);
    }
}
