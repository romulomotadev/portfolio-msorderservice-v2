package github.romulomotadev.msclients.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.romulomotadev.msclients.config.KeycloakJwtConverter;
import github.romulomotadev.msclients.config.SecurityConfig;
import github.romulomotadev.msclients.dto.ClientDto;
import github.romulomotadev.msclients.entities.Client;
import github.romulomotadev.msclients.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msclients.factory.ClientFactory;
import github.romulomotadev.msclients.service.ClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(ClientController.class)
@Import(SecurityConfig.class)
public class ClientControllerTests {

    //================== DEPENDÊNCIAS ==================

    @MockitoBean
    private ClientService clientService;
    @MockitoBean
    private KeycloakJwtConverter keycloakJwtConverter;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;


    //================== ATRIBUTOS ==================

    private ClientDto clientDto;
    private Long existingId;
    private Long nonExistingId;
    private String existingDocument;
    private String nonExistingDocument;
    private String jsonBody;


    //================== INICIALIZAÇÃO ==================


    @BeforeEach
    void setUp() throws JsonProcessingException {
        existingId = 1L;
        nonExistingId = 2L;
        existingDocument = "123.456.789-01";
        nonExistingDocument = "nonExistingDocument";

        //Factory
        Client client = ClientFactory.createClient();
        clientDto = new ClientDto(client);

        //Corpo da requisição
        jsonBody = objectMapper.writeValueAsString(clientDto);
    }


    // ================= GET =================

    //ID EXISTENTE
    @Test
    @DisplayName("Busca por ID deve retornar cliente quando ID existe")
    void findByIdShouldReturnClientDTOWhenIdExists() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(clientService.findById(existingId)).thenReturn(clientDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/clients/{id}", existingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("CLIENT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JASON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(clientDto.getName()))
                .andExpect(jsonPath("$.email").value(clientDto.getEmail()));

        //VERIFICAÇÃO
        Mockito.verify(clientService, Mockito.times(1))
                .findById(existingId);
    }

    //GET ID NÃO EXISTENTE
    @Test
    @DisplayName("Busca por ID deve lança Resource Not Found Exception quando ID não existe")
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(clientService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/clients/{id}", nonExistingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("CLIENT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }

    //BUSCA POR DOCUMENTO
    @Test
    @DisplayName("Busca por documento deve retornar cliente quando documento existir")
    void findByDocumentShouldReturnClientDTOWhenDocumentExists() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(clientService.findByPersonDocument(existingDocument)).thenReturn(clientDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/clients/document")
                        .param("document", existingDocument)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("CLIENT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JASON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(clientDto.getName()))
                .andExpect(jsonPath("$.email").value(clientDto.getEmail()))
                .andExpect(jsonPath("$.person.document").value(clientDto.getPerson().getDocument()));

        //VERIFICAÇÃO
        Mockito.verify(clientService, Mockito.times(1))
                .findByPersonDocument(existingDocument);
    }

    //BUSCA POR DOCUMENTO NÃO EXISTENTE
    @Test
    @DisplayName("Busca de cliente por documento lança Resource Not Found Exception quando documento não existe")
    void findByPersonDocumentThrowResourceNotFoundExceptionWhenDocumentDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(clientService.findByPersonDocument(nonExistingDocument)).thenThrow(ResourceNotFoundException.class);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/clients/document")
                        .param("document", nonExistingDocument)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("CLIENT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }

    //BUSCA TODOS CLIENTES
    @Test
    @DisplayName("Busca todos deve retorna Lista de clientes")
    void findAllShouldReturnListOfClientDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(clientService.findAll()).thenReturn(List.of(clientDto));

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/clients")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("CLIENT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JASON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(existingId))
                .andExpect(jsonPath("$[0].name").value(clientDto.getName()))
                .andExpect(jsonPath("$[0].email").value(clientDto.getEmail()));

        //VERIFICAÇÃO
        Mockito.verify(clientService, Mockito.times(1))
                .findAll();
    }


    // ================= DELETE =================

    //ID EXISTENTE
    @Test
    @DisplayName("Delete deve deletar client quando ID existir")
    void deleteShouldDeleteClientWhenIdExists() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.doNothing().when(clientService).delete(existingId);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(delete("/clients/{id}", existingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("CLIENT_WRITE")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNoContent());
    }

    // ID NÂO EXISTENTE
    @Test
    @DisplayName("Delete deve lançar Resource Not Found quando ID não existir")
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.doThrow(ResourceNotFoundException.class).when(clientService).delete(nonExistingId);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(delete("/clients/{id}", nonExistingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("CLIENT_WRITE")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }


    // ================= POST =================

    //DTO VALIDO
    @Test
    @DisplayName("Save deve retornar Client DTO quando DTO Valido")
    void saveShouldReturnClientDTOWhenDTOValid() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(clientService.save(ArgumentMatchers.any())).thenReturn(clientDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(post("/clients")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("CLIENT_WRITE")
                        ))
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JASON
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(clientDto.getName()))
                .andExpect(jsonPath("$.email").value(clientDto.getEmail()));

        //VERIFICAÇÃO
        Mockito.verify(clientService, Mockito.times(1))
                .save(ArgumentMatchers.any());
    }

    //DTO INVALIDO
    @Test
    @DisplayName("Save deve retornar Bad Request quando DTO invalido")
    void saveShouldReturnBadRequestWhenDTOInvalid() throws Exception {

        // PREPARA - DTO INVÁLIDO
        clientDto.setEmail(null);
        String jsonBodyInvalido = objectMapper.writeValueAsString(clientDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(post("/clients")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("CLIENT_WRITE")
                        ))
                        .content(jsonBodyInvalido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - STATUS HTTP
        resultActions.andExpect(status().isBadRequest());

        //VERIFICAÇÃO
        Mockito.verify(clientService, Mockito.never())
                .save(ArgumentMatchers.any());
    }


    // ================= PUT =================

    //ID VALIDO
    @Test
    @DisplayName("update deve atualizar retornar Client DTO quando ID valido")
    void updateShouldReturnClientDTOWhenIDValid() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(clientService.update(ArgumentMatchers.any(), ArgumentMatchers.eq(existingId))).thenReturn(clientDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(put("/clients/{id}", existingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("CLIENT_WRITE")
                        ))
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JASON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.name").value(clientDto.getName()))
                .andExpect(jsonPath("$.email").value(clientDto.getEmail()));

        //VERIFICAÇÃO
        Mockito.verify(clientService, Mockito.times(1))
                .update(ArgumentMatchers.any(), ArgumentMatchers.eq(existingId));
    }

    //ID NAO EXISTENTE
    //GET ID NÃO EXISTENTE
    @Test
    @DisplayName("update deve lança Resource Not Found Exception quando ID não existe")
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(clientService.update(ArgumentMatchers.any(), ArgumentMatchers.eq(nonExistingId))).thenThrow(ResourceNotFoundException.class);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(put("/clients/{id}", nonExistingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("CLIENT_WRITE")
                        ))
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }
}
