package github.romulomotadev.msorderservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import github.romulomotadev.msorderservice.config.KeycloakJwtConverter;
import github.romulomotadev.msorderservice.config.SecurityConfig;
import github.romulomotadev.msorderservice.dto.*;
import github.romulomotadev.msorderservice.entities.Order;
import github.romulomotadev.msorderservice.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msorderservice.factory.ClientDataResponseDtoFactory;
import github.romulomotadev.msorderservice.factory.OrderFactory;
import github.romulomotadev.msorderservice.factory.ProductDataResponseDtoFactory;
import github.romulomotadev.msorderservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
@Import(SecurityConfig.class)
public class OrderControllerTests {

    //================== DEPENDÊNCIAS ==================

    @MockitoBean
    private OrderService orderService;
    @MockitoBean
    private KeycloakJwtConverter keycloakJwtConverter;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    //================== ATRIBUTOS ==================

    private String existingDocument;
    private String nonExistingDocument;
    private String nameProduct;
    private String nameClient;
    private Long existingId;
    private Long nonExistingId;
    private String status;
    private OrderDto orderDto;
    private Page<OrderDto> orderDtoPage;
    private String jsonBody;

    private ClientDataResponseDTO clientDataResponseDto;
    private ProductDataResponseDTO productDataResponseDto;
    private Page<ProductResponseDto> pageResponse;

    //================== INICIALIZAÇÃO ==================

    @BeforeEach
    void setUp() throws JsonProcessingException {
        existingDocument = "111.111.111-11";
        nonExistingDocument = "222.222.222-22";
        nameProduct = "Produto 1";
        nameClient = "Ronaldo";
        existingId = 1L;
        nonExistingId = 2L;
        status = "created";

        // Factory Response / Cria os dados internos do cliente
        ClientResponseDto clientResponseDto = ClientDataResponseDtoFactory.createClientResponseDto();
        ProductResponseDto productResponseDto = ProductDataResponseDtoFactory.createProductResponseDto();

        //Factory
        Order order = OrderFactory.createOrder();
        orderDto = new OrderDto(order);

        //Paginação
        Pageable pageable = PageRequest.of(0, 10);
        pageResponse = new PageImpl<>(List.of(productResponseDto), pageable, 1);
        orderDtoPage = new PageImpl<>(List.of(orderDto), pageable, 1);

        // Cria o DTO externo
        clientDataResponseDto = new ClientDataResponseDTO();
        clientDataResponseDto.setClientResponseDto(clientResponseDto);
        productDataResponseDto = new ProductDataResponseDTO();
        productDataResponseDto.setProductResponseDto(pageResponse);

        //Corpo da requisição
        jsonBody = objectMapper.writeValueAsString(orderDto);
    }

    //================== GET RESPONSE ==================

    //BUSCA DOCUMENTO EXISTENTE RESPONSE
    @Test
    @DisplayName("getClientResponse deve retornar Client DTO quando documento existir")
    void getClientResponseShouldReturnClientDTOWhenDocumentExists() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        when(orderService.getClientResponse(existingDocument)).thenReturn(clientDataResponseDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/orders/document")
                        .param("document", existingDocument)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ORDER_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.clientResponseDto.id")
                        .value(clientDataResponseDto.getClientResponseDto().getId()))
                .andExpect(jsonPath("$.clientResponseDto.name")
                        .value(clientDataResponseDto.getClientResponseDto().getName()))
                .andExpect(jsonPath("$.clientResponseDto.person.document")
                        .value(existingDocument));

        //VERIFICAÇÃO
        Mockito.verify(orderService, Mockito.times(1))
                .getClientResponse(existingDocument);
    }

    //BUSCA DOCUMENTO INEXISTENTE RESPONSE
    @Test
    @DisplayName("getClientResponse deve retornar Not Found quando Documento não existe")
    void getClientResponseShouldReturnNotFoundWhenDocumentDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(orderService.getClientResponse(nonExistingDocument)).thenThrow(ResourceNotFoundException.class);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/orders/document")
                        .param("document", nonExistingDocument)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ORDER_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }

    //BUSCA PRODUTO RESPONSE
    @Test
    @DisplayName("Busca produto por nome deve retornar Product Response DTO")
    void searchProductByNameShouldReturnProductResponseDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(orderService.getProductResponse(ArgumentMatchers.eq(nameProduct), ArgumentMatchers.any())).thenReturn(productDataResponseDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/orders/search")
                        .param("name", nameProduct)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ORDER_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        // VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.productResponseDto.content[0].id")
                        .value(pageResponse.getContent().getFirst().getId()))
                .andExpect(jsonPath("$.productResponseDto.content[0].name")
                        .value(nameProduct))
                .andExpect(jsonPath("$.productResponseDto.totalElements")
                        .value(1));

        //VERIFICAÇÃO
        Mockito.verify(orderService, Mockito.times(1))
                .getProductResponse(ArgumentMatchers.eq(nameProduct), ArgumentMatchers.any());
    }

    //================== GET ==================

    //ID EXISTENTE
    @Test
    @DisplayName("Busca por ID deve retornar order DTO quando ID existe")
    void findByIdShouldReturnOrderDTOWhenIdExists() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(orderService.findById(existingId)).thenReturn(orderDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/orders/{id}", existingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ORDER_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.clientName").value(orderDto.getClientName()));

        //VERIFICAÇÃO
        Mockito.verify(orderService, Mockito.times(1))
                .findById(existingId);
    }

    //ID NÃO EXISTENTE
    @Test
    @DisplayName("Busca por ID deve lança Resource Not Found Exception quando ID não existe")
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(orderService.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/orders/{id}", nonExistingId)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ORDER_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO STATUS HTTP
        resultActions.andExpect(status().isNotFound());
    }

    //BUSCA TODOS PRODUTOS
    @Test
    @DisplayName("Busca todos deve retorna Page de orders DTO")
    void findAllShouldReturnListOfOrdersDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(orderService.findAll(ArgumentMatchers.any())).thenReturn(orderDtoPage);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/orders?page=0&size=10")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ORDER_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingId))
                .andExpect(jsonPath("$.content[0].clientName").value(orderDto.getClientName()));

        //VERIFICAÇÃO
        Mockito.verify(orderService, Mockito.times(1))
                .findAll(ArgumentMatchers.any());
    }

    // BUSCA TODOS ORDENS POR STATUS
    @Test
    @DisplayName("Busca todos Ordens por status retorna Page de orders DTO")
    void findAllOrdensByStatusShouldReturnPageOfOrdersDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(orderService.findByStatus(ArgumentMatchers.eq(status), ArgumentMatchers.any())).thenReturn(orderDtoPage);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/orders/status")
                        .param("status", status)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ORDER_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingId))
                .andExpect(jsonPath("$.content[0].clientName").value(orderDto.getClientName()));

        //VERIFICAÇÃO
        Mockito.verify(orderService, Mockito.times(1))
                .findByStatus(ArgumentMatchers.eq(status), ArgumentMatchers.any());
    }

    // BUSCA ORDENS PELO NOME DO CLIENTE
    @Test
    @DisplayName("Busca todas ordens pelo no nome do cliente retorna Page de orders DTO")
    void findAllOrdersByNameClientShouldReturnPageOfOrdersDTO() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(orderService.findByClient(ArgumentMatchers.eq(nameClient), ArgumentMatchers.any())).thenReturn(orderDtoPage);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(get("/orders/clientName")
                        .param("clientName", nameClient)
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ORDER_READ")
                        ))
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(existingId))
                .andExpect(jsonPath("$.content[0].clientName").value(orderDto.getClientName()));

        //VERIFICAÇÃO
        Mockito.verify(orderService, Mockito.times(1))
                .findByClient(ArgumentMatchers.eq(nameClient), ArgumentMatchers.any());
    }

    //============ POST ===============//

    //DTO VALIDO
    @Test
    @DisplayName("Criar order deve retornar order DTO quando DTO Valido")
    void createOrderShouldReturnORderDTOWhenDTOValid() throws Exception {

        //PREPARA - CHAMADA E RETORNO DO METODO
        Mockito.when(orderService.createOrder(ArgumentMatchers.any())).thenReturn(orderDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(post("/orders")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ORDER_WRITE")
                        ))
                        .content(jsonBody)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - DADOS ESPERADOS JSON
        resultActions.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(existingId))
                .andExpect(jsonPath("$.clientName").value(orderDto.getClientName()));

        //VERIFICAÇÃO
        Mockito.verify(orderService, Mockito.times(1))
                .createOrder(ArgumentMatchers.any());
    }

    //DTO INVALIDO
    @Test
    @DisplayName("Criar Order deve retornar Bad Request quando DTO invalido")
    void createOrderShouldReturnBadRequestWhenDTOInvalid() throws Exception {

        // PREPARA - DTO INVÁLIDO
        orderDto.setClientDocument(null);
        String jsonBodyInvalido = objectMapper.writeValueAsString(orderDto);

        //EXECUTA - CHAMADA DO ENDPOINT
        ResultActions resultActions =
                mockMvc.perform(post("/orders")
                        .with(jwt().authorities(
                                new SimpleGrantedAuthority("ORDER_WRITE")
                        ))
                        .content(jsonBodyInvalido)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        //VALIDAÇÃO - STATUS HTTP
        resultActions.andExpect(status().isBadRequest());

        //VERIFICAÇÃO
        Mockito.verify(orderService, Mockito.never())
                .createOrder(ArgumentMatchers.any());
    }
}
