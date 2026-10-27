package github.romulomotadev.msorderservice.service;

import github.romulomotadev.msorderservice.dto.*;
import github.romulomotadev.msorderservice.entities.Order;
import github.romulomotadev.msorderservice.entities.OrderItem;
import github.romulomotadev.msorderservice.entities.RequestStatus;
import github.romulomotadev.msorderservice.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msorderservice.response.ClientResponse;
import github.romulomotadev.msorderservice.repository.OrderRepository;
import github.romulomotadev.msorderservice.response.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

    private final ClientResponse clientResponse;
    private final ProductResponse productResponse;

    private final OrderRepository orderRepository;


    //============ GET ===============//

    // PARA TEST RESPONSE

    // RESPONSE CLIENT
    //busca cliente no microsserviço de clientes pelo documento
    public ClientDataResponseDTO getClientResponse(String document) {

        ResponseEntity<ClientResponseDto> clientResponseDto = clientResponse.findByPersonDocument(document);

        return ClientDataResponseDTO
                .builder()
                .clientResponseDto(clientResponseDto.getBody())
                .build();
    }

    //RESPONSE PRODUCT
    //busca produtos no microsserviço de produto por nome
    public ProductDataResponseDTO getProductResponse(String name, Pageable pageable) {

        ResponseEntity<Page<ProductResponseDto>> productResponseDto = productResponse.searchProductByName(name, pageable);

        return ProductDataResponseDTO
                .builder()
                .productResponseDto(productResponseDto.getBody())
                .build();
    }


    //============ GET ORDER SERVICE ===============//

    // BUSCA ORDEM POR ID
    public OrderDto findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Order not found with id: " + id));
        return new OrderDto(order);
    }

    // BUSCA TODAS ORDENS
    public Page<OrderDto> findAll(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(OrderDto::new);
    }

    // BUSCA TODAS AS ORDEM PELO STATUS
    public Page<OrderDto> findByStatus(String status, Pageable pageable) {
        Page<Order> ordersStatus = orderRepository.findByStatus(status, pageable);
        return ordersStatus.map(OrderDto::new);
    }

    // BUSCA TODAS AS ORDENS DO CLIENTE PELO NOME DO CLIENTE
    public Page<OrderDto> findByClient(String clientName, Pageable pageable) {
        Page<Order> ordersClient = orderRepository.findByClient(clientName, pageable);
        return ordersClient.map(OrderDto::new);
    }

    
    //============ POST ===============//

    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {

        // busca cliente por documento response
        ResponseEntity<ClientResponseDto> clientResponseDto =
                clientResponse.findByPersonDocument(orderDto.getClientDocument());

        // verifica se cliente / produto response existe na base de dados
        if (clientResponseDto.getBody() == null) {
            throw new ResourceNotFoundException("Client not found");
        }

        // cria nova ordem de serviço
        Order order = new Order();
        order.setClientName(clientResponseDto.getBody().getName());
        order.setClientDocument(clientResponseDto.getBody().getPerson().getDocument());
        order.setCreationDate(LocalDateTime.now());
        order.setRequestStatus(RequestStatus.CREATED);

        // lista de produtos da orem de serviço
        List<OrderItem> orderItems = new ArrayList<>();

        // adiciona produtos a lista de produtos
        for (OrderItemDto orderItemDto : orderDto.getOrderItem()) {

            //busca produto por nome response
            ResponseEntity<ProductResponseDto> productResponseDto =
                    productResponse.findByNameIgnoreCase(orderItemDto.getProductName());

            if (productResponseDto.getBody() == null) {
                throw new ResourceNotFoundException("Product not found: ");
            }

            // verificar ordem dto tem ao menos um item
            if (orderItemDto.getQuantity() == null || orderItemDto.getQuantity() <= 0) {
                throw new IllegalArgumentException("Item quantity must be greater than zero");
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(productResponseDto.getBody().getName());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setUniPrice(productResponseDto.getBody().getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalValue();

        Order savedOrder = orderRepository.save(order);

        return new OrderDto(savedOrder);
    }

}
