package github.romulomotadev.msorderservice.controller;

import github.romulomotadev.msorderservice.dto.ClientResponseDto;
import github.romulomotadev.msorderservice.dto.DataResponseDTO;
import github.romulomotadev.msorderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class orderController {

    private final OrderService orderService;


    //================== GET ====================

    @GetMapping("/document")
    public ResponseEntity<DataResponseDTO> clienteResponse(@RequestParam String document){
        DataResponseDTO clientResponseDto = orderService.getClientResponse(document);
        return ResponseEntity.ok(clientResponseDto);
    }

}
