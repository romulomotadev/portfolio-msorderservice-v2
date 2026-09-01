package github.romulomotadev.msorderservice.service;

import github.romulomotadev.msorderservice.dto.ClientResponseDto;
import github.romulomotadev.msorderservice.dto.DataResponseDTO;
import github.romulomotadev.msorderservice.repository.ClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final ClientResponse clientResponse;

    public DataResponseDTO getClientResponse(String document) {

        ResponseEntity<ClientResponseDto> clientResponseDto = clientResponse.findByPersonDocument(document);

        return DataResponseDTO
                .builder()
                .clientResponseDto(clientResponseDto.getBody())
                .build();
    }



}
