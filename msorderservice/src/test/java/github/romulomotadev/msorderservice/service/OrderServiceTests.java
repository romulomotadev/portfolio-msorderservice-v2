package github.romulomotadev.msorderservice.service;

import feign.FeignException;
import github.romulomotadev.msorderservice.dto.*;
import github.romulomotadev.msorderservice.entities.Order;
import github.romulomotadev.msorderservice.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msorderservice.repository.OrderRepository;
import github.romulomotadev.msorderservice.response.ClientResponse;
import github.romulomotadev.msorderservice.response.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static github.romulomotadev.msorderservice.factory.ClientDataResponseDtoFactory.createClientResponseDto;
import static github.romulomotadev.msorderservice.factory.OrderFactory.createOrder;
import static github.romulomotadev.msorderservice.factory.ProductDataResponseDtoFactory.createProductResponseDto;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTests {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private ClientResponse clientResponse;
    @Mock
    private ProductResponse productResponse;

    private String documentExisting;
    private String documentNotExisting;
    private String productNameExisting;
    private String productNameNotExisting;
    private Long orderIdExisting;
    private Long orderIdNotExisting;

    private PageImpl<ProductResponseDto> page;
    private PageImpl<ProductResponseDto> pageEmpty;
    private PageImpl<Order> pageOrder;
    private PageImpl<OrderDto> pageOrderDto;

    private ClientResponseDto clientResponseDto;
    private ProductResponseDto productResponseDto;
    private Order order;
    private OrderDto orderDto;


    @BeforeEach
    void setUp() {

        documentExisting = "111.111.111-11";
        documentNotExisting = "222.222.222-22";
        productNameExisting = "Produto 1";
        productNameNotExisting = "Produto 2";
        orderIdExisting = 1L;
        orderIdNotExisting = 2L;

        clientResponseDto = createClientResponseDto();
        productResponseDto = createProductResponseDto();
        order = createOrder();
        orderDto = new OrderDto(order);

        page = new PageImpl<>(List.of(productResponseDto));
        pageOrder = new PageImpl<>(List.of(order));
        pageOrderDto = new PageImpl<>(List.of(orderDto));

        pageEmpty = new PageImpl<>(List.of());
    }


    //============ GET CLIENT RESPONSE ===============//

    // BUSCA CLIENTE DOCUMENTO EXISTENTE
    @Test
    @DisplayName("getClientResponse deve retornar client response Dto quando Document existir")
    void getClientResponseShouldReturnClientResponseDtoWhenDocumentExists() {

        //PREPARAR
        when(clientResponse.findByPersonDocument(documentExisting)).thenReturn(ResponseEntity.ok(clientResponseDto));

        //EXECUTAR
        ClientDataResponseDTO response = orderService.getClientResponse(documentExisting);

        //VALIDAR
        assertEquals(response.getClientResponseDto().getId(), clientResponseDto.getId());
        assertEquals(response.getClientResponseDto().getName(), clientResponseDto.getName());
        assertEquals(response.getClientResponseDto().getPerson().getDocument(), clientResponseDto.getPerson().getDocument());
        assertEquals(response.getClientResponseDto().getAddresses().getFirst().getId(), clientResponseDto.getAddresses().getFirst().getId());
    }

    // BUSCA CLIENTE DOCUMENTO NÃO EXISTENTE
    @Test
    @DisplayName("getClientResponse deve lançar feign Client Exception quando documento não existir")
    void getClientResponseShouldThrowFeignClientExceptionWhenDocumentDoesNotExist() {

        //PREPARAR
        when(clientResponse.findByPersonDocument(documentNotExisting)).thenThrow(FeignException.FeignClientException.class);

        //EXECUTAR
        assertThrows(FeignException.FeignClientException.class,
                () -> orderService.getClientResponse(documentNotExisting));
    }


    //============ GET PRODUCT RESPONSE ===============//

    //BUSCA PRODUTO NOME EXISTENTE
    @Test
    @DisplayName("getProductResponse deve retornar product response Dto quando nome existir")
    void getProductResponseShouldReturnProductResponseDtoWhenNameExists() {

        //PREPARAR
        when(productResponse.searchProductByName(eq(productNameExisting), any(Pageable.class))).thenReturn(ResponseEntity.ok(page));
        Pageable pageable = PageRequest.of(0, 10);

        //EXECUTAR
        ProductDataResponseDTO response = orderService.getProductResponse(productNameExisting, pageable);

        //VALIDAR
        assertEquals(response.getProductResponseDto().getContent().getFirst().getId(), productResponseDto.getId());
        assertEquals(response.getProductResponseDto().getContent().getFirst().getName(), productResponseDto.getName());
        assertEquals(response.getProductResponseDto().getContent().getFirst().getDescription(), productResponseDto.getDescription());
    }

    //BUSCA PRODUTO NÃO EXISTENTE
    @Test
    @DisplayName("getProductResponse deve retornar page vazia quando não houver dados")
    void getProductResponseShouldReturnEmptyPageWhenNameDoesNotExist() {

        //PREPARAR
        when(productResponse.searchProductByName(eq(productNameNotExisting), any(Pageable.class))).thenReturn(ResponseEntity.ok(pageEmpty));
        Pageable pageable = PageRequest.of(0, 10);

        //EXECUTAR
        ProductDataResponseDTO response = orderService.getProductResponse(productNameNotExisting, pageable);

        //VALIDAR
        assertNotNull(response.getProductResponseDto());
        assertEquals(0, response.getProductResponseDto().getTotalElements());
        assertEquals(1, response.getProductResponseDto().getTotalPages());
        assertEquals(0, response.getProductResponseDto().getContent().size());
        assertTrue(response.getProductResponseDto().isEmpty());
    }


    //============ GET ORDER SERVICE ===============//

    //BUSCA ORDEM ID EXISTENTE
    @Test
    @DisplayName("findById deve retornar Order Dto quando Id existir")
    void findByIdShouldReturnOrderDtoWhenIdExists() {

        //PREPARAR
        when(orderRepository.findById(orderIdExisting)).thenReturn(Optional.of(order));

        //EXECUTAR
        OrderDto result = orderService.findById(orderIdExisting);

        //VALIDAR
        assertNotNull(result);
        assertEquals(orderDto.getId(), result.getId());
        assertEquals(orderDto.getClientName(), result.getClientName());
        assertEquals(orderDto.getOrderItem().getFirst().getId(), result.getOrderItem().getFirst().getId());
    }

    //BUSCAR ORDEM ID NÃO EXISTENTE
    @Test
    @DisplayName("findById deve lançar Resource Not Found Exception quando Order Id not exists")
    void findByIdThrowResourceNotFoundExceptionWhenOrderIdNotExistis(){

        //PREPARAR
        when(orderRepository.findById(orderIdNotExisting)).thenReturn(Optional.empty());

        //EXECUTAR
        assertThrows(ResourceNotFoundException.class,
                () -> orderService.findById(orderIdNotExisting));
    }

    //BUSCA TODAS AS ORDEM
    @Test
    @DisplayName("findAll deve retornar uma page de Order Dto quando existir ordens")
    void findAllShouldReturnPageOfOrderDtoWhenExistisOrders() {

        //PREPARAR
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(pageOrder);
        Pageable pageable = PageRequest.of(0, 10);

        //EXECUTAR
        Page<OrderDto> result = orderService.findAll(pageable);

        //VERIFICA
        assertNotNull(result);
        assertEquals(pageOrderDto.getTotalElements(), result.getTotalElements());
        assertEquals(pageOrderDto.getContent().getFirst().getId(), result.getContent().getFirst().getId());
        assertEquals(pageOrderDto.getContent().getFirst().getClientName(), result.getContent().getFirst().getClientName());
    }

    //BUSCAR TODAS ORDENS QUANDO VAZIA
    @Test
    @DisplayName("findAll deve retornar page vazia quando não houver order")
    void findAllShouldReturnPageEmptyWhenNotOrder(){

        //PREPARAR
        when(orderRepository.findAll(any(Pageable.class))).thenReturn(Page.empty());
        Pageable pageable = PageRequest.of(0, 10);

        //EXECUTAR
        Page<OrderDto> result = orderService.findAll(pageable);

        //VALIDAR
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getContent().size());
        assertTrue(result.isEmpty());
    }

}
