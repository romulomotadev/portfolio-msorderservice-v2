package github.romulomotadev.msorderservice.controller;

import github.romulomotadev.msorderservice.dto.ClientDataResponseDTO;
import github.romulomotadev.msorderservice.dto.OrderDto;
import github.romulomotadev.msorderservice.dto.ProductDataResponseDTO;
import github.romulomotadev.msorderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    //============ POST ===============//

    @PostMapping
    public ResponseEntity<OrderDto> createOrder(@RequestBody OrderDto orderDto){
        OrderDto orderDtoResponse = orderService.createOrder(orderDto);
        return ResponseEntity.ok(orderDtoResponse);
    }


}
