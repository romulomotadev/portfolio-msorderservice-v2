package github.romulomotadev.msorderservice.service;

import github.romulomotadev.msorderservice.dto.*;
import github.romulomotadev.msorderservice.entities.Order;
import github.romulomotadev.msorderservice.entities.OrderItem;
import github.romulomotadev.msorderservice.entities.RequestStatus;
import github.romulomotadev.msorderservice.exception.exceptions.ResourceNotFoundException;
import github.romulomotadev.msorderservice.repository.ClientResponse;
import github.romulomotadev.msorderservice.repository.OrderRepository;
import github.romulomotadev.msorderservice.repository.ProductResponse;
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

    // RESPONSE CLIENT
    public ClientDataResponseDTO getClientResponse(String document) {

        ResponseEntity<ClientResponseDto> clientResponseDto = clientResponse.findByPersonDocument(document);

        return ClientDataResponseDTO
                .builder()
                .clientResponseDto(clientResponseDto.getBody())
                .build();
    }

    //RESPONSE PRODUCT
    public ProductDataResponseDTO getProductResponse(String name, Pageable pageable) {

        ResponseEntity<Page<ProductResponseDto>> productResponseDto = productResponse.searchProductByName(name, pageable);

        return ProductDataResponseDTO
                .builder()
                .productResponseDto(productResponseDto.getBody())
                .build();
    }

    // FIND BY ID
    public OrderDto findById(Long id) {
        Order order = orderRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Order not found with id: " + id));
        return new OrderDto(order);
    }

    // FIND ALL
    public Page<OrderDto> findAll(Pageable pageable) {
        Page<Order> orders = orderRepository.findAll(pageable);
        return orders.map(OrderDto::new);
    }


    //============ POST ===============//

    @Transactional
    public OrderDto createOrder(OrderDto orderDto) {

        if (orderDto.getOrderItem() == null || orderDto.getOrderItem().isEmpty()) {
            throw new IllegalArgumentException("Order must have at least one item");
        }

        ResponseEntity<ClientResponseDto> clientResponseDto =
                clientResponse.findByPersonDocument(orderDto.getClientDocument());

        if (clientResponseDto.getBody() == null) {
            throw new ResourceNotFoundException("Client not found");
        }

        Order order = new Order();
        order.setClientName(clientResponseDto.getBody().getName());
        order.setClientDocument(clientResponseDto.getBody().getPerson().getDocument());
        order.setCreationDate(LocalDateTime.now());
        order.setRequestStatus(RequestStatus.CREATED);

        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemDto orderItemDto : orderDto.getOrderItem()) {

            if (orderItemDto.getQuantity() == null || orderItemDto.getQuantity() <= 0) {
                throw new IllegalArgumentException("Item quantity must be greater than zero");
            }

            ResponseEntity<Page<ProductResponseDto>> productResponseDto =
                    productResponse.searchProductByName(orderItemDto.getProductName(), Pageable.unpaged());

            if (productResponseDto.getBody() == null || productResponseDto.getBody().isEmpty() || productResponseDto.getBody().getContent().isEmpty()) {
                throw new ResourceNotFoundException("Product not found");
            }

            ProductResponseDto product = productResponseDto.getBody().getContent().getFirst();

            OrderItem orderItem = new OrderItem();
            orderItem.setProductName(product.getName());
            orderItem.setQuantity(orderItemDto.getQuantity());
            orderItem.setUniPrice(product.getPrice());
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }

        order.setOrderItems(orderItems);
        order.setTotalValue();

        Order savedOrder = orderRepository.save(order);

        return new OrderDto(savedOrder);
    }
}
