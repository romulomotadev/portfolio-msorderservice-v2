package github.romulomotadev.msorderservice.controller;

import github.romulomotadev.msorderservice.dto.ClientDataResponseDTO;
import github.romulomotadev.msorderservice.dto.OrderDto;
import github.romulomotadev.msorderservice.dto.ProductDataResponseDTO;
import github.romulomotadev.msorderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    //============ GET ===============//

    // RESPONSE CLIENT
    @GetMapping("/document")
    public ResponseEntity<ClientDataResponseDTO> clienteResponse(
            @RequestParam String document){
        ClientDataResponseDTO clientResponseDto = orderService.getClientResponse(document);
        return ResponseEntity.ok(clientResponseDto);
    }

    // RESPONSE PRODUCT
    @GetMapping("/search")
    public ResponseEntity<ProductDataResponseDTO> searchProductByName(
            @RequestParam String name, Pageable pageable){
        ProductDataResponseDTO productResponseDto = orderService.getProductResponse(name, pageable);
        return ResponseEntity.ok(productResponseDto);
    }

    // FIND BY ID
    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> findById(@PathVariable Long id){
        OrderDto orderDto = orderService.findById(id);
        return ResponseEntity.ok(orderDto);
    }

    // FIND ALL
    @GetMapping
    public ResponseEntity<Page<OrderDto>> findAll(Pageable pageable){
        Page<OrderDto> orderDtoPage = orderService.findAll(pageable);
        return ResponseEntity.ok(orderDtoPage);
    }

    // FIND BY ORDERS FOR STATUS
    @GetMapping("/status")
    public ResponseEntity<Page<OrderDto>> findByStatus(@RequestParam String status, Pageable pageable){
        Page<OrderDto> orderDtoPage = orderService.findByStatus(status, pageable);
        return ResponseEntity.ok(orderDtoPage);
    }

    // FIND BY ORDER FOR CLIENT
    @GetMapping("/clientName")
    public ResponseEntity<Page<OrderDto>> findByClient(@RequestParam String clientName, Pageable pageable){
        Page<OrderDto> orderDtoPage = orderService.findByClient(clientName, pageable);
        return ResponseEntity.ok(orderDtoPage);
    }


    //============ POST ===============//

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto){
        OrderDto dto = orderService.createOrder(orderDto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }


}
