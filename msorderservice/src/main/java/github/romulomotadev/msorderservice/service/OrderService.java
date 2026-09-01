package github.romulomotadev.msorderservice.service;

import github.romulomotadev.msorderservice.dto.ClientResponseDto;
import github.romulomotadev.msorderservice.dto.ClientDataResponseDTO;
import github.romulomotadev.msorderservice.dto.ProductDataResponseDTO;
import github.romulomotadev.msorderservice.dto.ProductResponseDto;
import github.romulomotadev.msorderservice.repository.ClientResponse;
import github.romulomotadev.msorderservice.repository.ProductResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ClientResponse clientResponse;
    private final ProductResponse productResponse;


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
}
