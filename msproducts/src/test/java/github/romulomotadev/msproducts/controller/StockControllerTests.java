package github.romulomotadev.msproducts.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.romulomotadev.msproducts.config.KeycloakJwtConverter;
import github.romulomotadev.msproducts.config.SecurityConfig;
import github.romulomotadev.msproducts.dto.StockDto;
import github.romulomotadev.msproducts.entities.Stock;
import github.romulomotadev.msproducts.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msproducts.factory.StockFactory;
import github.romulomotadev.msproducts.service.StockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StockController.class)
@Import(SecurityConfig.class)
public class StockControllerTests {

    //================== DEPENDÊNCIAS ==================

    @MockitoBean
    private StockService stockService;
    @MockitoBean
    private KeycloakJwtConverter keycloakJwtConverter;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    //================== ATRIBUTOS ==================

    private StockDto stockDto;
    private Long existingId;
    private Long nonExistingId;
    private String jsonBody;

    //================== INICIALIZAÇÃO ==================

    @BeforeEach
    void setUp() throws JsonProcessingException {
        existingId = 1L;
        nonExistingId = 2L;

        //Factory
        Stock stock = StockFactory.createdStock();
        stockDto = new StockDto(stock);

        //Corpo da requisição
        jsonBody = objectMapper.writeValueAsString(stockDto);
    }

    //================== GET ==================

    //ID EXISTENTE
    @Test
    @DisplayName("Busca por ID deve retornar stock DTO quando ID existe")
    void findByIdShouldReturnStockDTOWhenIdExists() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(stockService.findById(existingId)).thenReturn(stockDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/stocks/{id}", existingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.quantity").value(stockDto.getQuantity()));

        //VERIFICAÇÃO
        Mockito.verify(stockService, Mockito.times(1))
                .findById(existingId);
    }

    //GET ID NÃO EXISTENTE
    @Test
    @DisplayName("Busca por ID deve lança Resource Not Found Exception quando ID não existe")
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(stockService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/stocks/{id}", nonExistingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }

    //BUSCA ESTOQUE COMPLETO
    @Test
    @DisplayName("Busca todos deve retorna Lista de stock DTO")
    void findAllShouldReturnListOfStockDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(stockService.findAll()).thenReturn(List.of(stockDto));

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/stocks")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(existingId))
                .andExpect(jsonPath("$[0].quantity").value(stockDto.getQuantity()));

        //VERIFICAÇÃO
        Mockito.verify(stockService, Mockito.times(1))
                .findAll();
    }

    // ================= PUT =================

    //ID VALIDO
    @Test
    @DisplayName("update deve atualizar retornar Stock DTO quando ID valido")
    void updateStockShouldReturnStockDTOWhenIDValid() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(stockService.update(ArgumentMatchers.any(), ArgumentMatchers.eq(existingId))).thenReturn(stockDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(put("/stocks/{id}", existingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_WRITE")
                        ))
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.quantity").value(stockDto.getQuantity()));

        //VERIFICAÇÃO
        Mockito.verify(stockService, Mockito.times(1))
                .update(ArgumentMatchers.any(), ArgumentMatchers.eq(existingId));
    }

    //ID NAO EXISTENTE
    @Test
    @DisplayName("update deve lança Resource Not Found Exception quando ID não existe")
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(stockService.update(ArgumentMatchers.any(), ArgumentMatchers.eq(nonExistingId))).thenThrow(ResourceNotFoundException.class);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(put("/stocks/{id}", nonExistingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("PRODUCT_WRITE")
                        ))
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }
}
